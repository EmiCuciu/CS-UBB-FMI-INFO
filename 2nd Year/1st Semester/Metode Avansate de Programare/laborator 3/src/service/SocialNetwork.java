package service;

import Utils.Graf;
import domain.Prietenie;
import domain.User;
import repository.InMemoryRepository;

public class SocialNetwork {

    private final InMemoryRepository<Long, User> repositoryUser;
    private final InMemoryRepository<Long, Prietenie> repositoryPrietenie;
    private final Graf graf;

    public SocialNetwork(InMemoryRepository<Long, User> repositoryUser, InMemoryRepository<Long, Prietenie> repositoryPrietenie) {
        this.repositoryUser = repositoryUser;
        this.repositoryPrietenie = repositoryPrietenie;
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
    }

    public Iterable<User> getUsers() {
        return repositoryUser.findAll();
    }

    public void updateUser(User user) {
        repositoryUser.update(user);
    }

    public void updatePrietenie(Prietenie prietenie) {
        repositoryPrietenie.update(prietenie);
    }

    public User findUser(Long id) {
        return repositoryUser.findOne(id);
    }

    public Long getNewUserID() {
        Long id = 0L;
        for (User user : repositoryUser.findAll())
            id = user.getId();
        id++;
        return id;
    }

    public Prietenie findPrietenie(Long id) {
        return repositoryPrietenie.findOne(id);
    }

    public void addUser(User user) {
        user.setId(getNewUserID());
        repositoryUser.save(user);
        graf.addNode(user.getId());
    }

    public Iterable<Prietenie> getPrietenii() {
        return repositoryPrietenie.findAll();
    }

    public void addPrietenie(Prietenie prietenie) {
        repositoryPrietenie.save(prietenie);
        graf.addEdge(prietenie.getIdUser1(), prietenie.getIdUser2());
    }

    public void removePrietenie(Long idUser1, Long idUser2) {
        graf.removeEdge(idUser1, idUser2);
        for (Prietenie prietenie : getPrietenii()) {
            if (prietenie.getIdUser1().equals(idUser1) && prietenie.getIdUser2().equals(idUser2)) {
                repositoryPrietenie.delete(prietenie.getId());
            }
        }
        System.out.println("Prietenia a fost stearsa cu succes!");
    }

    public void removeUser(Long id) {
        User user = repositoryUser.findOne(id);
        if (user == null) {
            throw new IllegalArgumentException("Nu exista user cu acest id");
        }

        for (Prietenie prietenie : getPrietenii()) {
            if (prietenie.getIdUser1().equals(id) || prietenie.getIdUser2().equals(id)) {
                repositoryPrietenie.delete(prietenie.getId());
                graf.removeEdge(prietenie.getIdUser1(), prietenie.getIdUser2());
            }
        }

        repositoryUser.delete(id);
        graf.removeNode(id);
    }

    public Graf getGraf() {
        return graf;
    }

    public void printFriendshipGraph() {
        graf.printGraph();
    }
}