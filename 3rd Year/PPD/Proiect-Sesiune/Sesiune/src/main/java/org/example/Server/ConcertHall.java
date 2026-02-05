package org.example.Server;

import org.example.Model.*;
import org.example.Repository.database.DatabaseManager;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcertHall {
    private final ReentrantLock lock = new ReentrantLock();
    private final DatabaseManager db;

    // In-memory state (fast access)
    private final Map<Integer, Show> shows = new ConcurrentHashMap<>();
    private final Map<Integer, Set<Integer>> soldSeats = new ConcurrentHashMap<>();
    private final Map<Integer, Set<Integer>> reservedSeats = new ConcurrentHashMap<>();  // Locuri rezervate temporar
    private final Map<Integer, Reservation> pendingReservations = new ConcurrentHashMap<>();
    private volatile double totalBalance = 0.0;     // volatile pentru vizibilitate intre thread-uri

    private final int maxSeats;

    public ConcertHall(int maxSeats, DatabaseManager db) throws SQLException {
        this.maxSeats = maxSeats;
        this.db = db;
        loadFromDatabase();
    }

    private void loadFromDatabase() throws SQLException {
        List<Show> loadedShows = db.loadShows();
        for (Show s : loadedShows) {
            shows.put(s.getId(), s);
            soldSeats.put(s.getId(), new HashSet<>());  // inițializare goală
            reservedSeats.put(s.getId(), new HashSet<>());  // rezervări active
        }

        // Load sold seats (doar PAID)
        Map<Integer, Set<Integer>> dbSeats = db.loadSoldSeats();
        soldSeats.putAll(dbSeats);

        // Load balance
        totalBalance = db.loadTotalBalance();

        System.out.println("════════════════════════════════════════");
        System.out.println("ConcertHall loaded from DB:");
        System.out.println("  - Shows: " + shows.size());
        for (Show show : shows.values()) {
            int soldCount = soldSeats.getOrDefault(show.getId(), new HashSet<>()).size();
            System.out.println("    • " + show.getTitle() + " (S" + show.getId() + "): " +
                             soldCount + "/" + maxSeats + " seats sold, price=" + show.getPricePerTicket());
        }
        System.out.println("  - Total balance: " + totalBalance + " RON");
        System.out.println("════════════════════════════════════════\n");
    }

    public Response checkAndReserve(int showId, List<Integer> requestedSeats, int clientId) {
        lock.lock();
        try {
            // Validare show exists
            if (!shows.containsKey(showId)) {
                return new Response(ResponseType.SHOW_NOT_FOUND, "Show ID " + showId + " not found");
            }

            // Validare seats în range [1, maxSeats]
            if (requestedSeats.stream().anyMatch(s -> s < 1 || s > maxSeats)) {
                return new Response(ResponseType.INVALID_SEATS, "Seats must be between 1 and " + maxSeats);
            }

            // Check availability (sold + reserved)
            Set<Integer> occupied = soldSeats.get(showId);
            Set<Integer> reserved = reservedSeats.get(showId);
            List<Integer> occupiedRequested = requestedSeats.stream()
                .filter(seat -> occupied.contains(seat) || reserved.contains(seat))
                .toList();

            if (!occupiedRequested.isEmpty()) {
                return new Response(ResponseType.SEATS_OCCUPIED, "Seats already occupied: " + occupiedRequested);
            }

            // Client are deja o rezervare activă?
            if (pendingReservations.containsKey(clientId)) {
                return new Response(ResponseType.CLIENT_HAS_PENDING_RESERVATION,
                    "Client already has a pending reservation");
            }

            // ── Rezervare in-memory ──
            Reservation res = new Reservation(showId, requestedSeats, clientId, System.currentTimeMillis());
            reservedSeats.get(showId).addAll(requestedSeats);

            // ── Rezervare in DB ──
            try {
                int saleId = db.insertReservation(showId, requestedSeats);
                res.setSaleId(saleId);  // link între in-memory și DB
                pendingReservations.put(clientId, res);
            } catch (SQLException e) {
                // Rollback in-memory
                reservedSeats.get(showId).removeAll(requestedSeats);

                // Check dacă e UNIQUE constraint violation (race condition)
                if (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed")) {
                    return new Response(ResponseType.SEATS_OCCUPIED, "Seats already occupied (race condition)");
                }
                return new Response(ResponseType.DB_ERROR, "Database error: " + e.getMessage());
            }

            return new Response(ResponseType.SEATS_AVAILABLE, (java.io.Serializable) requestedSeats);

        } finally {
            lock.unlock();
        }
    }

    public Response processPayment(int clientId) {
        lock.lock();
        try {
            Reservation res = pendingReservations.remove(clientId);

            if (res == null) {
                return new Response(ResponseType.NO_RESERVATION_FOUND, "No reservation found for client " + clientId);
            }

            if (res.isExpired(10_000)) {  // T_max = 10s
                // Clean expired reservation
                Set<Integer> reserved = reservedSeats.get(res.getShowId());
                if (reserved != null) {
                    reserved.removeAll(res.getSeats());
                }
                cleanReservation(res);
                return new Response(ResponseType.RESERVATION_EXPIRED, "Reservation expired (T_max=10s)");
            }

            // Confirm vânzare
            Show show = shows.get(res.getShowId());
            double amount = res.getSeats().size() * show.getPricePerTicket();

            // ── Update in-memory ──
            reservedSeats.get(res.getShowId()).removeAll(res.getSeats());
            soldSeats.get(res.getShowId()).addAll(res.getSeats());
            totalBalance += amount;

            // ── Update DB ──
            try {
                db.confirmPayment(res.getSaleId(), amount);
            } catch (SQLException e) {
                // Rollback in-memory (critical!)
                soldSeats.get(res.getShowId()).removeAll(res.getSeats());
                reservedSeats.get(res.getShowId()).addAll(res.getSeats());
                totalBalance -= amount;
                pendingReservations.put(clientId, res);  // restaurează rezervarea
                return new Response(ResponseType.DB_ERROR, "Database error: " + e.getMessage());
            }

            return new Response(ResponseType.PAYMENT_SUCCESS, amount);

        } finally {
            lock.unlock();
        }
    }

    // CLEANING expired reservations (apelat periodic din Server)
    public void cleanExpiredReservations() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            List<Integer> toRemove = new ArrayList<>();

            for (Map.Entry<Integer, Reservation> entry : pendingReservations.entrySet()) {
                if (entry.getValue().isExpired(10_000)) {
                    toRemove.add(entry.getKey());
                }
            }

            if (!toRemove.isEmpty()) {
                for (int clientId : toRemove) {
                    Reservation res = pendingReservations.remove(clientId);
                    if (res != null) {
                        // Eliberează locurile rezervate
                        Set<Integer> reserved = reservedSeats.get(res.getShowId());
                        if (reserved != null) {
                            reserved.removeAll(res.getSeats());
                        }
                        cleanReservation(res);
                        System.out.println("[CLEANUP] Expired reservation removed: client=" + clientId +
                                         ", sale=" + res.getSaleId() + ", seats=" + res.getSeats());
                    }
                }
            }

        } finally {
            lock.unlock();
        }
    }

    private void cleanReservation(Reservation res) {
        try {
            db.deleteExpiredReservation(res.getSaleId());
        } catch (SQLException e) {
            System.err.println("Failed to clean reservation from DB: " + e.getMessage());
        }
    }

    public Map<Integer, Set<Integer>> getSoldSeats() {
        // Return deep copy pentru a evita modificări externe
        Map<Integer, Set<Integer>> copy = new HashMap<>();
        for (Map.Entry<Integer, Set<Integer>> e : soldSeats.entrySet()) {
            copy.put(e.getKey(), new HashSet<>(e.getValue()));
        }
        return copy;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public Map<Integer, Show> getShows() {
        return new HashMap<>(shows);
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public DatabaseManager getDb() {
        return db;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public int getPendingReservationsCount() {
        return pendingReservations.size();
    }
}
