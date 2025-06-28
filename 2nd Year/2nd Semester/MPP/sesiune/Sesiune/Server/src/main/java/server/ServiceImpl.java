package server;

import domain.Joc;
import domain.Jucator;
import domain.RaspunsJucator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.repository.IRepositories.IRepoJocuri;
import persistence.repository.IRepositories.IRepoJucatori;
import persistence.repository.IRepositories.IRepoRaspunsuri;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ServiceImpl implements IServices {

    private IRepoJucatori repoJucatori;
    private IRepoJocuri repoJocuri;
    private IRepoRaspunsuri repoRaspunsuri;

    private Map<String, IObserver> loggedJucatori;

    private static Logger logger = LogManager.getLogger(ServiceImpl.class);

    public ServiceImpl(IRepoJucatori jucatoriRepo, IRepoJocuri jocuriRepo, IRepoRaspunsuri raspunsuriRepo) {
        this.repoJucatori = jucatoriRepo;
        this.repoJocuri = jocuriRepo;
        this.repoRaspunsuri = raspunsuriRepo;
        this.loggedJucatori = new HashMap<>();
    }

    @Override
    public void logIn(Jucator jucator, IObserver observer) {
        Optional<Jucator> jucatorOptional = repoJucatori.findByNume(jucator.getnume());
        if (jucatorOptional.isPresent()) {
            Jucator existing = jucatorOptional.get();
            if(loggedJucatori.containsKey(existing.getnume())) {
                throw new ServiceException("Jucatorul este deja conectat!");
            }
            jucator.setId(existing.getId());
            loggedJucatori.put(jucator.getnume(), observer);
            logger.info("Jucator logged in: {}", jucator.getnume());
        }
        else {
            throw new ServiceException("Jucatorul nu a fost gasit!");
        }
    }

    @Override
    public void logOut(Jucator jucator, IObserver observer) {
        loggedJucatori.remove(jucator.getnume());
        logger.info("Jucator logged out: {}", jucator.getnume());
    }

    @Override
    public List<Joc> getJocuriByJucator(Jucator jucator) {
        logger.info("Getting games for jucator: {}", jucator.getnume());
        List<Joc> jocuri = repoJocuri.findByJucator(jucator);
        logger.info("Found {} games for jucator", jocuri.size());
        return jocuri;
    }

    @Override
    public List<RaspunsJucator> getRaspunsuriByJoc(Joc joc) {
        logger.info("Getting answers for joc: {}", joc.getId());
        List<RaspunsJucator> raspunsuri = repoRaspunsuri.findByJoc(joc);
        logger.info("Found {} answers for joc", raspunsuri.size());
        return raspunsuri;
    }
}