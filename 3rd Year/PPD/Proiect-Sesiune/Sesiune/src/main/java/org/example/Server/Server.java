package org.example.Server;

import org.example.Client.Client;
import org.example.Model.VerificationResult;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Server {
    private final ExecutorService threadPool;
    private final ScheduledExecutorService scheduler;
    private final ConcertHall hall;
    private final VerificationService verificationService;
    private final int verificationIntervalSeconds;
    private volatile boolean running = true;
    private List<Client> clients = new ArrayList<>();

    public Server(ConcertHall hall, int verificationIntervalSeconds) {
        this.hall = hall;
        this.verificationIntervalSeconds = verificationIntervalSeconds;
        this.threadPool = Executors.newFixedThreadPool(8);  // 8 workers
        this.scheduler = Executors.newScheduledThreadPool(2);  // 1 pentru verificare, 1 pentru cleaning
        this.verificationService = new VerificationService(hall);
    }

    public void start() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("Server started.");
        System.out.println("  - ThreadPool size: 8 workers");
        System.out.println("  - Verification interval: " + verificationIntervalSeconds + "s");
        System.out.println("  - T_max (reservation timeout): 10s");
        System.out.println("  - Server runtime: 180s (3 minutes)");
        System.out.println("════════════════════════════════════════\n");

        // ── Task 1: Verificare periodică ──
        scheduler.scheduleAtFixedRate(
            this::runVerification,
            verificationIntervalSeconds,
            verificationIntervalSeconds,
            TimeUnit.SECONDS
        );

        // ── Task 2: Cleaning rezervări expirate ──
        scheduler.scheduleAtFixedRate(
            hall::cleanExpiredReservations,
            2, 2, TimeUnit.SECONDS
        );

        // ── Task 3: Shutdown after 3 minutes ──
        scheduler.schedule(this::shutdown, 180, TimeUnit.SECONDS);
    }

    private void runVerification() {
        try {
            System.out.println("\n[VERIFICATION] Running consistency check...");
            VerificationResult result = verificationService.verify();
            saveVerificationResult(result);
            System.out.println("[VERIFICATION] Result: " + result.getStatus());
        } catch (Exception e) {
            System.err.println("[VERIFICATION] Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveVerificationResult(VerificationResult result) {
        String filename = "verification_" + verificationIntervalSeconds + "s.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            writer.println(result.toLogString());
        } catch (IOException e) {
            System.err.println("Failed to write verification result: " + e.getMessage());
        }
    }

    private void shutdown() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("Server shutting down after 3 minutes...");
        System.out.println("════════════════════════════════════════\n");

        running = false;

        // Notifică clienții activi
        System.out.println("Notifying " + clients.size() + " active clients...");
        clients.forEach(Client::notifyShutdown);

        // Shutdown thread pool
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("ThreadPool did not terminate in time, forcing shutdown...");
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Shutdown scheduler
        scheduler.shutdown();

        // Final verification
        System.out.println("\n[FINAL VERIFICATION] Running final consistency check...");
        VerificationResult finalResult = verificationService.verify();
        saveVerificationResult(finalResult);

        System.out.println("\n════════════════════════════════════════");
        System.out.println("Final verification: " + finalResult.getStatus());
        System.out.println("  - Total balance: " + finalResult.getTotalBalance() + " RON");
        System.out.println("  - Seats match: " + finalResult.isSeatsMatch());
        System.out.println("  - Balance match: " + finalResult.isBalanceMatch());
        System.out.println("  - Sales consistent: " + finalResult.isSalesConsistent());
        System.out.println("════════════════════════════════════════\n");

        // Close DB
        try {
            hall.getDb().close();
        } catch (SQLException e) {
            System.err.println("Failed to close DB: " + e.getMessage());
        }

        System.out.println("Server shutdown complete.");
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public boolean isRunning() {
        return running;
    }

    public ConcertHall getHall() {
        return hall;
    }
}
