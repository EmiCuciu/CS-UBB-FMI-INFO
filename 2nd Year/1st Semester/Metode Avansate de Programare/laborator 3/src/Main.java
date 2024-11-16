import domain.Prietenie;
import domain.User;
import domain.validators.ValidatorPrietenie;
import domain.validators.ValidatorUser;
import repository.InMemoryRepository;
import service.SocialNetwork;
import ui.Console;

public class Main {

    public static void main(String[] args) {


        InMemoryRepository<Long, User> repoUser = new InMemoryRepository<>(new ValidatorUser());
        InMemoryRepository<Long, Prietenie> repoFriendship = new InMemoryRepository<>(new ValidatorPrietenie());


        SocialNetwork socialNetwork = new SocialNetwork(repoUser, repoFriendship);
        Console ui = new Console(socialNetwork);

        User u1 = new User("Alex", "Popescu");
        User u2 = new User("Bogdan", "Ionescu");
        User u3 = new User("Cornel", "Georgescu");
        User u4 = new User("Dan", "Popa");
        User u5 = new User("Eusebiu", "Dimitrie");
        User u6 = new User("Fane", "Sportivu");
        User u7 = new User("Adrian", "Banditul");
        User u8 = new User("Horia", "Greu deucis");

        socialNetwork.addUser(u1);
        socialNetwork.addUser(u2);
        socialNetwork.addUser(u3);
        socialNetwork.addUser(u4);
        socialNetwork.addUser(u5);
        socialNetwork.addUser(u6);
        socialNetwork.addUser(u7);
        socialNetwork.addUser(u8);

        ui.run();
    }
}
