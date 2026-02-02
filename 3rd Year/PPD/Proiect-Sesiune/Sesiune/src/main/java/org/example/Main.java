package org.example;

import org.example.Client.Client;
import org.example.Repository.database.DatabaseManager;
import org.example.Server.ConcertHall;
import org.example.Server.Server;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║      CONCERT HALL TICKET SALES SYSTEM                 ║");
            System.out.println("║      Client-Server with Concurrency & Thread Pool     ║");
            System.out.println("╚════════════════════════════════════════════════════════╝\n");

            // ── 1. Inițializează DB ──
            System.out.println("Initializing database...");
            DatabaseManager db = new DatabaseManager();

            // ── 2. Inițializează ConcertHall (load din DB) ──
            System.out.println("Initializing ConcertHall...");
            ConcertHall hall = new ConcertHall(100, db);

            // ── 3. Pornește Server ──
            int verificationInterval = args.length > 0 ? Integer.parseInt(args[0]) : 5;  // default 5s
            Server server = new Server(hall, verificationInterval);
            server.start();

            // ── 4. Pornește 10 clienți ──
            System.out.println("Starting 10 clients...");
            List<Client> clients = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Client client = new Client(i, server.getThreadPool(), hall);
                clients.add(client);
                new Thread(client, "Client-" + i).start();
            }
            server.setClients(clients);

            System.out.println("\n════════════════════════════════════════");
            System.out.println("System running.");
            System.out.println("  - 10 clients generating requests every 2s");
            System.out.println("  - ThreadPool: 8 workers");
            System.out.println("  - Verification interval: " + verificationInterval + "s");
            System.out.println("  - Server will shutdown in 180s (3 minutes)");
            System.out.println("════════════════════════════════════════\n");

        } catch (SQLException e) {
            System.err.println("\n╔════════════════════════════════════════════════════════╗");
            System.err.println("║  FATAL ERROR: Failed to initialize database          ║");
            System.err.println("╚════════════════════════════════════════════════════════╝\n");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("\n╔════════════════════════════════════════════════════════╗");
            System.err.println("║  FATAL ERROR: System initialization failed           ║");
            System.err.println("╚════════════════════════════════════════════════════════╝\n");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

