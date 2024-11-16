package src;

import src.domain.Prietenie;
import src.domain.User;
import src.domain.validators.ValidatorPrietenie;
import src.domain.validators.ValidatorUser;
import src.repository.database.FriendshipDBRepo;
import src.repository.database.UserDBRepo;
//import src.repository.file.PrietenieFileRepository;
//import src.repository.file.UserFileRepo;
import src.repository.memory.InMemoryRepository;
import src.service.SocialNetwork;
import src.ui.Console;

public class Main {

    public static void main(String[] args) {


        SocialNetwork socialNetwork = getSocialNetwork();
        Console ui = new Console(socialNetwork);

        User u1 = new User("Alex", "Popescu");
        User u2 = new User("Bogdan", "Ionescu");
        User u3 = new User("Cornel", "Georgescu");
        User u4 = new User("Dan", "Popa");
        User u5 = new User("Eusebiu", "Dimitrie");
        User u6 = new User("Fane", "Sportivu");
        User u7 = new User("Adrian", "Banditul");
        User u8 = new User("Horia", "Greu deucis");

//        socialNetwork.addUser(u1);
//        socialNetwork.addUser(u2);
//        socialNetwork.addUser(u3);
//        socialNetwork.addUser(u4);
//        socialNetwork.addUser(u5);
//        socialNetwork.addUser(u6);
//        socialNetwork.addUser(u7);
//        socialNetwork.addUser(u8);

        ui.run();
    }

    private static SocialNetwork getSocialNetwork() {
        InMemoryRepository<Long, User> repoUser = new InMemoryRepository<>(new ValidatorUser());
        InMemoryRepository<Long, Prietenie> repoFriendship = new InMemoryRepository<>(new ValidatorPrietenie());

//        UserFileRepo userFileRepo = new UserFileRepo(new ValidatorUser(), "Z:\\Faculta\\MAP JAVA\\Laboratoare\\lab4\\src\\src\\users.txt");
//        PrietenieFileRepository prietenieFileRepository = new PrietenieFileRepository(new ValidatorPrietenie(), "Z:\\Faculta\\MAP JAVA\\Laboratoare\\lab4\\src\\src\\friendships.txt");

        UserDBRepo userDB = new UserDBRepo(new ValidatorUser(), "users");
        FriendshipDBRepo friendshipDB = new FriendshipDBRepo(new ValidatorPrietenie(), "friendships");

        return new SocialNetwork(repoUser, repoFriendship, userDB, friendshipDB);
    }
}
