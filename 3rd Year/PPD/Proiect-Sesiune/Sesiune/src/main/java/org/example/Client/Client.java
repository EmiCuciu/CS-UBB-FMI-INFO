package org.example.Client;

import org.example.Model.Response;
import org.example.Model.ResponseType;
import org.example.Server.ConcertHall;

import java.util.*;
import java.util.concurrent.*;

public class Client implements Runnable {
    private final int id;
    private final ExecutorService serverPool;
    private final ConcertHall hall;
    private volatile boolean active = true;
    private final Random random = new Random();

    public Client(int id, ExecutorService pool, ConcertHall hall) {
        this.id = id;
        this.serverPool = pool;
        this.hall = hall;
    }

    @Override
    public void run() {
        System.out.println("Client " + id + " started.");

        while (active) {
            try {
                // ── Generează cerere aleatorie ──
                int showId = random.nextInt(1, 4);  // S1=1, S2=2, S3=3
                int numSeats = random.nextInt(1, 6);  // 1-5 bilete
                List<Integer> seats = generateRandomSeats(numSeats);

                System.out.println("Client " + id + " requesting: show=S" + showId + ", seats=" + seats);

                // ── Submit task la ThreadPool ──
                ClientTask task = new ClientTask(hall, id, showId, seats);
                Future<Response> future = serverPool.submit(task);

                // ── Așteaptă rezultat cu timeout ──
                Response response = future.get(15, TimeUnit.SECONDS);  // > T_max pentru siguranță

                logResponse(response);

                // ── Sleep 2 secunde înainte de următoarea cerere ──
                Thread.sleep(2000);

            } catch (TimeoutException e) {
                System.err.println("Client " + id + ": request timeout!");
            } catch (InterruptedException e) {
                System.out.println("Client " + id + ": interrupted.");
                break;
            } catch (ExecutionException e) {
                System.err.println("Client " + id + ": execution error - " + e.getCause());
            }
        }

        System.out.println("Client " + id + " stopped.");
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
                System.out.println("Client " + id + ": seats reserved, processing payment...");
                break;
            case PAYMENT_SUCCESS:
                System.out.println("Client " + id + ": payment SUCCESS, amount=" + response.getData() + " RON");
                break;
            case SEATS_OCCUPIED:
                System.out.println("Client " + id + ": seats OCCUPIED - " + response.getData());
                break;
            case RESERVATION_EXPIRED:
                System.out.println("Client " + id + ": reservation EXPIRED");
                break;
            case SHOW_NOT_FOUND:
                System.out.println("Client " + id + ": show NOT FOUND");
                break;
            case INVALID_SEATS:
                System.out.println("Client " + id + ": INVALID seats");
                break;
            case CLIENT_HAS_PENDING_RESERVATION:
                System.out.println("Client " + id + ": already has PENDING reservation");
                break;
            case NO_RESERVATION_FOUND:
                System.out.println("Client " + id + ": NO reservation found");
                break;
            case DB_ERROR:
                System.err.println("Client " + id + ": DATABASE ERROR - " + response.getData());
                break;
            default:
                System.out.println("Client " + id + ": response=" + response.getType());
        }
    }

    public void notifyShutdown() {
        System.out.println("Client " + id + " received shutdown notification.");
        this.active = false;
    }
}
