package triatlon.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triatlon.model.*;
import triatlon.persistence.IArbitruRepository;
import triatlon.persistence.IParticipantRepository;
import triatlon.persistence.IProbaRepository;
import triatlon.persistence.IRezultatRepository;
import triatlon.services.ITriatlonObserver;
import triatlon.services.ITriatlonServices;
import triatlon.services.TriatlonException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TriatlonServicesImpl implements ITriatlonServices {
    private final IArbitruRepository arbitruRepository;
    private final IRezultatRepository rezultatRepository;
    private final IParticipantRepository participantRepository;
    private final IProbaRepository probaRepository;
    private static final Logger logger = LogManager.getLogger(TriatlonServicesImpl.class);

    private Map<String, ITriatlonObserver> loggedClients;

    public TriatlonServicesImpl(IArbitruRepository arbitruRepo, IRezultatRepository rezultatRepo,
                                IParticipantRepository participantRepo, IProbaRepository probaRepo) {
        arbitruRepository = arbitruRepo;
        rezultatRepository = rezultatRepo;
        participantRepository = participantRepo;
        probaRepository = probaRepo;
        loggedClients = new ConcurrentHashMap<>();
    }

    // Authentication
    @Override
    public synchronized void login(Arbitru arbitru, ITriatlonObserver client) throws TriatlonException {
        Iterable<Arbitru> aribtrii = arbitruRepository.findAll();
        for (Arbitru a : aribtrii) {
            if (a.getUsername().equals(arbitru.getUsername()) && a.getPassword().equals(arbitru.getPassword())) {
                loggedClients.put(arbitru.getUsername(), client);
                logger.info("Arbitru {} logged in", arbitru.getUsername());
                notifyArbitriiLoggedIn(arbitru);
            }
        }
    }

    private final int defaultThreadsNo = 3;

    private void notifyArbitriiLoggedIn(Arbitru arbitru) throws TriatlonException {
        Iterable<Arbitru> arbitrus = arbitruRepository.findAll();
        logger.debug("Notifying all arbitrii about the logged in arbitru::  " + arbitru.getUsername());

        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (Arbitru a : arbitrus) {
            if (!a.getUsername().equals(arbitru.getUsername())) {
                executor.execute(() -> {
                    try {
                        ITriatlonObserver client = loggedClients.get(a.getUsername());
                        if (client != null) {
                            client.arbitruLoggedIn(arbitru);
                        }
                    } catch (TriatlonException e) {
                        logger.error("Error notifying arbitru {}: {}", a.getUsername(), e.getMessage());
                    }
                });
            }
        }
        executor.shutdown();
    }


    @Override
    public synchronized void logout(Arbitru arbitru, ITriatlonObserver client) throws TriatlonException {
        ITriatlonObserver localClient = loggedClients.remove(arbitru.getUsername());
        if (localClient == null) {
            logger.warn("Arbitru {} is not logged in", arbitru.getUsername());
            throw new TriatlonException("Arbitru " + arbitru.getUsername() + " is not logged in");
        }
        logger.info("Arbitru {} logged out", arbitru.getUsername());
        notifyArbitriiLoggedOut(arbitru);
    }

    private void notifyArbitriiLoggedOut(Arbitru arbitru) {
        logger.debug("Notifying all arbitrii about the logged out arbitru" + arbitru.getUsername());
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (Map.Entry<String, ITriatlonObserver> entry : loggedClients.entrySet()) {
            executor.execute(() -> {
                try {
                    entry.getValue().arbitruLoggedOut(arbitru);
                } catch (TriatlonException e) {
                    logger.error("Error notifying arbitru {}: {}", entry.getKey(), e.getMessage());
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public synchronized boolean register(String username, String password, String firstName, String lastName) throws TriatlonException {
        Iterable<Arbitru> arbitri = arbitruRepository.findAll();
        for (Arbitru a : arbitri) {
            if (a.getUsername().equals(username)) {
                logger.warn("Username {} already exists", username);
                return false;
            }
        }
        Arbitru newArbitru = new Arbitru(0, username, password, firstName, lastName);
        arbitruRepository.save(newArbitru);
        logger.info("Arbitru {} registered successfully", username);
        return true;
    }


    // Rezultat Service
    @Override
    public synchronized void addRezultat(Participant participant, Arbitru arbitru, TipProba tipProba, int punctaj) throws TriatlonException {
        logger.info("Adding rezultat for participant {}: {} points in {}", participant.getId(), punctaj, tipProba);
        Rezultat rezultat = new Rezultat(null, participant, arbitru, tipProba, punctaj);
        rezultatRepository.save(rezultat);

        participant.setPunctajProba(tipProba, punctaj);

        logger.info("Rezultat added: {}", rezultat);
        notifyRezultatAdded(rezultat);
    }

    private void notifyRezultatAdded(Rezultat rezultat) {
        logger.debug("Notifying all arbitrii about the added rezultat" + rezultat);
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for (Map.Entry<String, ITriatlonObserver> entry : loggedClients.entrySet()) {
            executor.execute(() -> {
                try {
                    entry.getValue().rezultatAdded(rezultat);
                } catch (TriatlonException e) {
                    logger.error("Error notifying arbitru {}: {}", entry.getKey(), e.getMessage());
                }
            });
        }
        executor.shutdown();
    }

    @Override
    public synchronized List<Rezultat> getResultateForProba(TipProba tipProba) throws TriatlonException {
        List<Rezultat> rezultate = StreamSupport.stream(rezultatRepository.findAll().spliterator(), false)
                .filter(r -> r.getTipProba() == tipProba)
                .sorted(Comparator.comparing(Rezultat::getPunctaj).reversed())
                .collect(Collectors.toList());
        return rezultate;
    }

    @Override
    public synchronized List<Rezultat> getAllResults() {
        return List.of();
    }

    // Participant Service
    @Override
    public synchronized List<Participant> getAllParticipants() throws TriatlonException {
        List<Participant> participants = new ArrayList<>();
        participantRepository.findAll().forEach(participants::add);
        participants.sort(Comparator.comparing(Participant::getLast_name)
                .thenComparing(Participant::getFirst_name));
        return participants;
    }

    @Override
    public synchronized int calculateTotalScore(Participant participant) throws TriatlonException {
        if (participant == null) return 0;

        int total = 0;
        // Get all results for this participant from the result repository
        for (Rezultat rezultat : rezultatRepository.findAll()) {
            if (rezultat.getParticipant().getId().equals(participant.getId())) {
                // Update the score in the participant's map
                participant.setPunctajProba(rezultat.getTipProba(), rezultat.getPunctaj());
                total += rezultat.getPunctaj();
            }
        }
        return total;
    }


    // Proba Service
    @Override
    public synchronized List<Proba> getAllProbe() throws TriatlonException {
        List<Proba> probe = new ArrayList<>();
        probaRepository.findAll().forEach(probe::add);
        return probe;
    }

    @Override
    public synchronized TipProba getProbaForArbitru(Arbitru arbitru) throws TriatlonException {
        for (Proba proba : getAllProbe()) {
            if (proba.getArbitru().getId().equals(arbitru.getId())) {
                System.out.println("Proba GASITA: " + proba.getTipProba());
                return proba.getTipProba();
            }
        }
        return null;
    }

    @Override
    public Arbitru findArbitruByUsernameAndPassword(String username, String password) {
        return arbitruRepository.findBy(username, password);
    }
}
