package src.service;

import src.Utils.Graf;
import src.domain.Prietenie;
import src.domain.User;
import src.repository.database.FriendshipDBRepo;
import src.repository.database.UserDBRepo;
//import src.repository.file.PrietenieFileRepository;
//import src.repository.file.UserFileRepo;
import src.repository.memory.InMemoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SocialNetwork {

    private final InMemoryRepository<Long, User> repositoryUser;
    private final InMemoryRepository<Long, Prietenie> repositoryPrietenie;
    private final Graf graf;
//    private final UserFileRepo userFileRepo;
//    private final PrietenieFileRepository prietenieFileRepository;
    private final UserDBRepo userDB;
    private final FriendshipDBRepo friendshipDB;

    public SocialNetwork(InMemoryRepository<Long, User> repositoryUser, InMemoryRepository<Long, Prietenie> repositoryPrietenie, UserDBRepo userDB, FriendshipDBRepo friendshipDB) {
        this.repositoryUser = repositoryUser;
        this.repositoryPrietenie = repositoryPrietenie;
//        this.userFileRepo = userFileRepo;
//        this.prietenieFileRepository = prietenieFileRepository;
        this.userDB = userDB;
        this.friendshipDB = friendshipDB;
        this.graf = new Graf();
        initializeGraph();
    }

    private void initializeGraph() {
        for (User user : repositoryUser.findAll()) {
            graf.addNode(user.getId());
        }
        for (Prietenie prietenie : repositoryPrietenie.findAll()) {
            graf.addEdge(prietenie.getIdUser1(), prietenie.getIdUser2());
        }
        loadDatafromDB();
    }

    public void loadDatafromDB() {
        for (User user : userDB.findAll()) {
            repositoryUser.save(user);
            graf.addNode(user.getId());
        }

        for (Prietenie prietenie : friendshipDB.findAll()) {
            repositoryPrietenie.save(prietenie);
            graf.addEdge(prietenie.getIdUser1(), prietenie.getIdUser2());
        }
    }

    public Iterable<User> getUsers() {
        return repositoryUser.findAll();
    }

    public void updateUser(User user) {
        repositoryUser.update(user);
//        userFileRepo.update(user);
    }

    public void updatePrietenie(Prietenie prietenie) {
        repositoryPrietenie.update(prietenie);
//        prietenieFileRepository.update(prietenie);
    }

    public Optional<User> findUser(Long id) {
        return repositoryUser.findOne(id);
    }

    public Long getNewUserID() {
        Long id = 0L;
        for (User user : repositoryUser.findAll())
            id = user.getId();
        id++;
        return id;
    }

    public Optional<Prietenie> findPrietenie(Long id) {
        return repositoryPrietenie.findOne(id);
    }

    public void addUser(User user) {
        user.setId(getNewUserID());
        repositoryUser.save(user);
//        userFileRepo.save(user);
        userDB.save(user);
        graf.addNode(user.getId());
    }

    public Iterable<Prietenie> getPrietenii() {
        return repositoryPrietenie.findAll();
    }

    public void addPrietenie(Prietenie prietenie) {
        repositoryPrietenie.save(prietenie);
//        prietenieFileRepository.save(prietenie);
        friendshipDB.save(prietenie);
        graf.addEdge(prietenie.getIdUser1(), prietenie.getIdUser2());
    }

    public void removePrietenie(Long idUser1, Long idUser2) {
        List<Prietenie> toRemove = new ArrayList<>();
        for (Prietenie prietenie : getPrietenii()) {
            if (prietenie.getIdUser1().equals(idUser1) && prietenie.getIdUser2().equals(idUser2)) {
                toRemove.add(prietenie);
            }
        }
        for (Prietenie prietenie : toRemove) {
            repositoryPrietenie.delete(prietenie.getId());
            graf.removeEdge(prietenie.getIdUser1(), prietenie.getIdUser2());
//            prietenieFileRepository.removePrietenie(idUser1, idUser2);
            friendshipDB.removePrietenie(idUser1, idUser2);
        }
        System.out.println("Prietenia a fost stearsa cu succes!");
    }

    public void removeUser(Long id) {
        Optional<User> user = repositoryUser.findOne(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Nu exista user cu acest id");
        }

        List<Prietenie> toRemove = new ArrayList<>();
        for (Prietenie prietenie : getPrietenii()) {
            if (prietenie.getIdUser1().equals(id) || prietenie.getIdUser2().equals(id)) {
                toRemove.add(prietenie);
            }
        }

        for (Prietenie prietenie : toRemove) {
            repositoryPrietenie.delete(prietenie.getId());
            graf.removeEdge(prietenie.getIdUser1(), prietenie.getIdUser2());
        }

        repositoryUser.delete(id);
        userDB.removeUser(id);
        graf.removeNode(id);
    }

    public Graf getGraf() {
        return graf;
    }

    public void printFriendshipGraph() {
        graf.printGraph();
    }
}