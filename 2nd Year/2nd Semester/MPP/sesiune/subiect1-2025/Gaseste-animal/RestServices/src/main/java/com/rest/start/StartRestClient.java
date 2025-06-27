package com.rest.start;

import com.model.Game;
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

    public static void Main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = LocalDateTime.now().format(formatter);

        Player player = new Player();
        player.setAlias("RESTplayer");

        try {
            // Visualize Games for a Player
            System.out.println("\nVisualizing games for player with alias = " + player.getAlias());
            show(() -> {
                Game[] games = client.getGamesByPlayer(player);
                Arrays.sort(games, Comparator.comparing(Game::getNoOfSeconds).reversed());

                for(Game game: games){
                    System.out.println("Game ID: " + game.getId());
                    System.out.println("Player Alias: " + game.getPlayer().getAlias());
                    Position[] positions = client.getPositionsByGame(game);
                    for (Position position: positions)
                        if (position.getPositionIndex() % 2 == 0)
                            System.out.println("O Position " + position.getPositionIndex() + ": (" + position.getCoordinateX() + ", " + position.getCoordinateY() + ")");
                        else
                            System.out.println("X Position " + position.getPositionIndex() + ": (" + position.getCoordinateY() + ", " + position.getCoordinateX() + ")");
                    System.out.println("Score: " + game.getScore());
                    System.out.println("Time in Seconds: " + game.getNoOfSeconds() + "\n");
                }
            });

            // Add Position
            System.out.println("\nAdding position ...");
            Game game = client.getGameById(2L);
            Position position = new Position(game, 2L, 1L, 3L);
            show(() -> {
                Position savedPosition = client.addPosition(position);
                System.out.println("Saved position: " + savedPosition);
            });

        } catch (RestClientException ex) {
            System.out.println("Exception... " + ex.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("ServiceException " + e);
        }
    }
}
