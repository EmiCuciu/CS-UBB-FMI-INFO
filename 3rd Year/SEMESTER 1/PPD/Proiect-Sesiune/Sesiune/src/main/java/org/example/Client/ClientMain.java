package org.example.Client;

import org.example.Model.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientMain {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8080;

    private final int clientId;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final Random random = new Random();
    private volatile boolean running = true;

    public ClientMain(int clientId) {
        this.clientId = clientId;
    }

    public void connect() throws IOException {
        socket = new Socket(SERVER_HOST, SERVER_PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
        output.flush();
        input = new ObjectInputStream(socket.getInputStream());

        output.writeInt(clientId);
        output.flush();

        System.out.println("Client " + clientId + " connected to server at " +
            SERVER_HOST + ":" + SERVER_PORT);
    }

    public void start() {
        System.out.println("Client " + clientId + " started.");

        while (running) {
            try {
                int showId = random.nextInt(1, 4);  // S1=1, S2=2, S3=3
                int numSeats = random.nextInt(1, 6);  // 1-5 bilete
                List<Integer> seats = generateRandomSeats(numSeats);

                System.out.println("Client " + clientId + " requesting: show=S" +
                    showId + ", seats=" + seats);

                Request request = new Request(RequestType.PURCHASE, showId, seats);

                output.writeObject(request);
                output.flush();

                Response response = (Response) input.readObject();

                logResponse(response);

                Thread.sleep(2000);

            } catch (EOFException e) {
                System.out.println("Client " + clientId + ": Server closed connection.");
                break;
            } catch (IOException e) {
                System.err.println("Client " + clientId + ": Connection error - " + e.getMessage());
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Client " + clientId + ": Invalid response format - " + e.getMessage());
                break;
            } catch (InterruptedException e) {
                System.out.println("Client " + clientId + ": interrupted.");
                break;
            }
        }

        cleanup();
        System.out.println("Client " + clientId + " stopped.");
    }

    private List<Integer> generateRandomSeats(int count) {
        Set<Integer> seats = new HashSet<>();
        while (seats.size() < count) {
            seats.add(random.nextInt(1, 101));  // 1-100
        }
        return new ArrayList<>(seats);
    }

    private void logResponse(Response response) {
        switch (response.getType()) {
            case SEATS_AVAILABLE:
                System.out.println("Client " + clientId + ": seats reserved, processing payment...");
                break;
            case PAYMENT_SUCCESS:
                System.out.println("Client " + clientId + ": payment SUCCESS, amount=" +
                    response.getData() + " RON");
                break;
            case SEATS_OCCUPIED:
                System.out.println("Client " + clientId + ": seats OCCUPIED - " + response.getData());
                break;
            case RESERVATION_EXPIRED:
                System.out.println("Client " + clientId + ": reservation EXPIRED");
                break;
            case SHOW_NOT_FOUND:
                System.out.println("Client " + clientId + ": show NOT FOUND");
                break;
            case INVALID_SEATS:
                System.out.println("Client " + clientId + ": INVALID seats");
                break;
            case CLIENT_HAS_PENDING_RESERVATION:
                System.out.println("Client " + clientId + ": already has PENDING reservation");
                break;
            case NO_RESERVATION_FOUND:
                System.out.println("Client " + clientId + ": NO reservation found");
                break;
            case DB_ERROR:
                System.err.println("Client " + clientId + ": DATABASE ERROR - " + response.getData());
                break;
            default:
                System.out.println("Client " + clientId + ": response=" + response.getType());
        }
    }

    private void cleanup() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Client " + clientId + ": Error during cleanup - " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java ClientMain <clientId>");
            System.err.println("Example: java ClientMain 1");
            System.exit(1);
        }

        int clientId = Integer.parseInt(args[0]);
        ClientMain client = new ClientMain(clientId);

        try {
            client.connect();
            client.start();
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
