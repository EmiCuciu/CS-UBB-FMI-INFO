package org.example.Server;

import org.example.Model.PerformanceMetrics;
import org.example.Model.Response;
import org.example.Model.VerificationResult;
import org.example.Repository.database.DatabaseManager;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.concurrent.*;

/**
 * Server Principal cu Socket pentru conexiuni client-server reale
 * Rulează pe port 8080 și acceptă conexiuni de la clienți remote
 */
public class ServerMain {
    private static final int PORT = 8080;
    private static final int MAX_CLIENTS = 50;

    private final ServerSocket serverSocket;
    private final ExecutorService clientHandlerPool;      // Thread pool pentru clienți
    private final ScheduledExecutorService scheduler;     // Verificare + cleanup
    private final ConcertHall hall;
    private final VerificationService verificationService;
    private final PerformanceMetrics metrics;             // Metrici de performanță
    private final int verificationIntervalSeconds;
    private volatile boolean running = true;
    private final long serverStartTime;

    public ServerMain(ConcertHall hall, int verificationIntervalSeconds) throws IOException {
        this.hall = hall;
        this.verificationIntervalSeconds = verificationIntervalSeconds;
        this.serverSocket = new ServerSocket(PORT);
        this.clientHandlerPool = Executors.newFixedThreadPool(MAX_CLIENTS);
        this.scheduler = Executors.newScheduledThreadPool(2);
        this.verificationService = new VerificationService(hall);
        this.metrics = new PerformanceMetrics();
        this.serverStartTime = System.currentTimeMillis();

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║      CONCERT HALL TICKET SALES SERVER                 ║");
        System.out.println("║      Real Client-Server with Socket Communication     ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
        System.out.println("Server listening on port: " + PORT);
    }

    public void start() {
        System.out.println("\n════════════════════════════════════════");
        System.out.println("Server started.");
        System.out.println("  - Port: " + PORT);
        System.out.println("  - Max clients: " + MAX_CLIENTS);
        System.out.println("  - ThreadPool size: " + MAX_CLIENTS + " workers");
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

        // ── Accept client connections ──
        acceptClients();
    }

    private void acceptClients() {
        System.out.println("Waiting for client connections...\n");

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[NEW CONNECTION] Client connected from: " +
                    clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

                // Creează un handler pentru client și îl trimite la thread pool
                ClientHandler handler = new ClientHandler(clientSocket, hall, metrics);
                clientHandlerPool.submit(handler);

            } catch (SocketException e) {
                if (!running) {
                    System.out.println("Server socket closed, stopping accept loop.");
                    break;
                }
                System.err.println("Socket error: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
        }
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

        try {
            serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }

        // Shutdown client handler pool
        clientHandlerPool.shutdown();
        try {
            if (!clientHandlerPool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Client handler pool did not terminate in time, forcing shutdown...");
                clientHandlerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            clientHandlerPool.shutdownNow();
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

        // Save performance metrics
        savePerformanceMetrics();

        System.out.println("\n════════════════════════════════════════");
        System.out.println("PERFORMANCE METRICS:");
        System.out.println("  - Total requests: " + metrics.getTotalRequests());
        System.out.println("  - Successful: " + metrics.getSuccessfulRequests());
        System.out.println("  - Failed: " + metrics.getFailedRequests());
        System.out.println("  - Success rate: " + String.format("%.2f%%", metrics.getSuccessRate()));
        System.out.println("  - Average response time: " + String.format("%.2f ms", metrics.getAverageResponseTime()));
        System.out.println("  - Throughput: " + String.format("%.2f requests/sec", metrics.getThroughput()));
        System.out.println("  - Total runtime: " + metrics.getElapsedTimeSeconds() + " seconds");
        System.out.println("════════════════════════════════════════\n");

        // Close DB
        try {
            hall.getDb().close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.err.println("Failed to close DB: " + e.getMessage());
        }

        System.out.println("Server shutdown complete.");
        System.exit(0);
    }

    private void savePerformanceMetrics() {
        String filename = "performance_metrics_" + verificationIntervalSeconds + "s.csv";
        boolean fileExists = new File(filename).exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename, true))) {
            if (!fileExists) {
                // Write header
                writer.println("timestamp,avg_response_time_ms,throughput_req_per_sec,success_rate_percent,total_requests,successful_requests,failed_requests");
            }
            writer.println(metrics.toCSV());
            System.out.println("Performance metrics saved to: " + filename);
        } catch (IOException e) {
            System.err.println("Failed to save performance metrics: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Initializing database...");
            DatabaseManager db = new DatabaseManager();

            System.out.println("Initializing ConcertHall...");
            ConcertHall hall = new ConcertHall(100, db);

            int verificationInterval = args.length > 0 ? Integer.parseInt(args[0]) : 5;
            ServerMain server = new ServerMain(hall, verificationInterval);
            server.start();

        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
