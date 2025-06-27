package com.rest.start;

import com.model.Joc;
import com.model.Player;
import com.model.Position;
import com.rest.client.Client;
import com.rest.services.ServiceException;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class StartRestClient {
    private final static Client client = new Client();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> handleGetGame(scanner);
                case "2" -> handleGetPositionsForGame(scanner);
                case "3" -> handleGetGamesByPlayer(scanner);
                case "4" -> handleAddPosition(scanner);
                case "0" -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Get game by ID");
        System.out.println("2. Get positions for game");
        System.out.println("3. Get games by player");
        System.out.println("4. Add position");
        System.out.println("0. Exit");
        System.out.print("Choose option: ");
    }

    private static void handleGetGame(Scanner scanner) {
        System.out.print("Enter game ID: ");
        try {
            Integer id = Integer.parseInt(scanner.nextLine());
            Joc game = client.getGameById(id);
            if (game != null) {
                System.out.println("Game found: " + game.getId());
                System.out.println("Player: " + game.getPlayer().getPorecla());
                System.out.println("Configuration: " + game.getConfiguratie());
                System.out.println("Start time: " + game.getStartingTime());
                System.out.println("Number of tries: " + game.getNrOfTries());
            }
        } catch (ServiceException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format");
        }
    }

    private static void handleGetPositionsForGame(Scanner scanner) {
        System.out.print("Enter game ID: ");
        try {
            Integer id = Integer.parseInt(scanner.nextLine());
            Joc game = client.getGameById(id);
            if (game != null) {
                Position[] positions = client.getPositionsByGame(game);
                for (Position position : positions) {
                    if (Integer.parseInt(position.getId().toString()) % 2 == 0) {
                        System.out.println("O Position " + position.getId() + ": (" + position.getX() + ", " + position.getY() + ")");
                    } else {
                        System.out.println("X Position " + position.getId() + ": (" + position.getX() + ", " + position.getY() + ")");
                    }
                }
                System.out.println("Number of tries: " + game.getNrOfTries());
            }
        } catch (ServiceException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid ID format");
        }
    }

    private static void handleGetGamesByPlayer(Scanner scanner) {
        System.out.print("Enter player nickname: ");
        String nickname = scanner.nextLine();
        try {
            Player player = new Player();
            player.setPorecla(nickname);

            Joc[] games = client.getGamesByPlayer(player);
            if (games != null && games.length > 0) {
                Arrays.sort(games, Comparator.comparing(Joc::getStartingTime).reversed());

                for (Joc game : games) {
                    System.out.println("Game ID: " + game.getId());
                    System.out.println("Player: " + game.getPlayer().getPorecla());
                    System.out.println("Start time: " + game.getStartingTime());
                    System.out.println("Number of tries: " + game.getNrOfTries());
                    System.out.println("--------------------");
                }
            } else {
                System.out.println("No games found for player: " + nickname);
            }
        } catch (ServiceException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void handleAddPosition(Scanner scanner) {
        try {
            System.out.print("Enter game ID: ");
            Integer gameId = Integer.parseInt(scanner.nextLine());

            Joc game = client.getGameById(gameId);
            if (game == null) {
                System.err.println("Game not found");
                return;
            }

            System.out.print("Enter X coordinate: ");
            int x = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Y coordinate: ");
            int y = Integer.parseInt(scanner.nextLine());

            Position position = new Position(null, x, y);
            Position addedPosition = client.addPosition(position);

            if (addedPosition != null) {
                System.out.println("Position added successfully!");
                System.out.println("ID: " + addedPosition.getId());
                System.out.println("Coordinates: (" + addedPosition.getX() + ", " + addedPosition.getY() + ")");
            } else {
                System.err.println("Failed to add position");
            }
        } catch (ServiceException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format");
        }
    }
}