package src.ui;

import src.FileUtil;
import src.domain.Prietenie;
import src.domain.User;
import src.domain.validators.ValidationException;
import src.service.SocialCommunities;
import src.service.SocialNetwork;

import java.util.List;
import java.util.Scanner;

public class Console {

    private final SocialCommunities socialCommunities;
    private final SocialNetwork socialNetwork;

    public Console(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
        this.socialCommunities = new SocialCommunities(socialNetwork);
        registerShutdownHook();
    }

    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileUtil.clearFile("src/src/users.txt");
            FileUtil.clearFile("src/src/friendships.txt");
        }));
    }

    void printMenu() {
        System.out.println("\t\t\tMENU\t\t\t");
        System.out.println("1. Add user");
        System.out.println("2. Remove user");
        System.out.println("3. Add friendship");
        System.out.println("4. Remove friendship");
        System.out.println("5. Print users");
        System.out.println("6. Communities");
        System.out.println("7. Most Social Community");
        System.out.println("0. EXIT");
    }

    public void run() {
        Scanner scan = new Scanner(System.in);
        boolean ok = true;
        while (ok) {
            printMenu();
            String input = scan.nextLine();
            switch (input) {
                case "1":
                    addUser();
                    break;
                case "2":
                    removeUser();
                    break;
                case "3":
                    addPrietenie();
                    break;
                case "4":
                    removePrietenie();
                    break;
                case "5":
                    printUsers();
                    break;
                case "6":
                    printConnectedCommunities();
                    break;
                case "7":
                    printMostSocialCommunity();
                    break;
                case "11":
                    socialNetwork.printFriendshipGraph();
                    break;
                case "0":
                    System.out.println("exit");
                    ok = false;
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }
    }

    void printUsers() {
        System.out.println("\t\t\tUSERS\t\t\t");
        for (User u : socialNetwork.getUsers()) {
            System.out.println("ID: " + u.getId() + " " + u.getFirstName() + " " + u.getLastName());
        }
    }

    void printFriendships() {
        System.out.println("\t\t\tFRIENDSHIPS\t\t\t");
        socialNetwork.printFriendshipGraph();
    }

    void addUser() {
        System.out.println("Add user");
        Scanner scan = new Scanner(System.in);
        System.out.println("First name: ");
        String firstName = scan.nextLine();
        System.out.println("Last name: ");
        String lastName = scan.nextLine();
        User user = new User(firstName, lastName);
        socialNetwork.addUser(user);
        System.out.println("User added: " + user.getId() + " " + user.getFirstName() + " " + user.getLastName());
    }

    void removeUser() {
        printUsers();
        System.out.println("Remove user");
        Scanner scan = new Scanner(System.in);
        System.out.println("Id: ");
        String var = scan.nextLine();
        try {
            Long id = Long.parseLong(var);
            socialNetwork.removeUser(id);
            System.out.println("User with ID: " + id + " was removed.");
        } catch (IllegalArgumentException e) {
            System.out.println("ID must be a number! It can't contain letters or symbols! ");
        }
    }

    void addPrietenie() {
        printUsers();
        printFriendships();
        Scanner scan = new Scanner(System.in);
        System.out.println("ID of the first user: ");
        String var1 = scan.nextLine();
        System.out.println("ID of the second user: ");
        String var2 = scan.nextLine();
        try {
            Long id1 = 0L, id2 = 0L;
            try {
                id1 = Long.parseLong(var1);
                id2 = Long.parseLong(var2);
            } catch (IllegalArgumentException e) {
                System.out.println("ID must be a number! It can't contain letters or symbols! ");
            }
            socialNetwork.addPrietenie(new Prietenie(id1, id2));
        } catch (ValidationException e) {
            System.out.println("Friendship is invalid! ");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments! ");
        }
        socialNetwork.printFriendshipGraph();
    }

    private void removePrietenie() {
        Scanner scan = new Scanner(System.in);
        System.out.println("ID of the first user: ");
        String var1 = scan.nextLine();
        System.out.println("ID of the second user: ");
        String var2 = scan.nextLine();
        try {
            Long id1 = 0L, id2 = 0L;
            try {
                id1 = Long.parseLong(var1);
                id2 = Long.parseLong(var2);
            } catch (IllegalArgumentException e) {
                System.out.println("ID must be a number! It can't contain letters or symbols! ");
            }
            socialNetwork.removePrietenie(id1, id2);
        } catch (ValidationException e) {
            System.out.println("Friendship is invalid! ");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments! ");
        }
    }

    private void printConnectedCommunities() {
        System.out.println("Social Communities\n");
        int nrOfCommunities = socialCommunities.connectedCommunities();
        System.out.println("Number of Social Communities: " + nrOfCommunities);
    }

    private void printMostSocialCommunity() {
        System.out.println("Most social community: ");
        List<User> mostSocialCommunity = socialCommunities.mostSocialCommunity();
        for (User user : mostSocialCommunity) {
            System.out.println("ID: " + user.getId() + " Name: " + user.getFirstName() + " " + user.getLastName());
        }
    }
}