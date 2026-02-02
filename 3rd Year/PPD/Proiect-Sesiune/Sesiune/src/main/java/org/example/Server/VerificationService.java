package org.example.Server;

import org.example.Model.Sale;
import org.example.Model.SaleStatus;
import org.example.Model.VerificationResult;
import org.example.Repository.database.DatabaseManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class VerificationService {
    private final ConcertHall hall;

    public VerificationService(ConcertHall hall) {
        this.hall = hall;
    }

    public VerificationResult verify() {
        hall.getLock().lock();
        try {
            DatabaseManager db = hall.getDb();

            // ── 1. Recompută sold seats din DB ──
            Map<Integer, Set<Integer>> dbSeats = db.loadSoldSeats();
            Map<Integer, Set<Integer>> memorySeats = hall.getSoldSeats();
            boolean seatsMatch = dbSeats.equals(memorySeats);

            if (!seatsMatch) {
                System.err.println("[VERIFICATION DEBUG] Seats mismatch details:");
                for (int showId = 1; showId <= 3; showId++) {
                    Set<Integer> dbSet = dbSeats.getOrDefault(showId, new HashSet<>());
                    Set<Integer> memSet = memorySeats.getOrDefault(showId, new HashSet<>());
                    System.err.println("  Show " + showId + ": DB=" + dbSet.size() + " seats, Memory=" + memSet.size() + " seats");
                    if (!dbSet.equals(memSet)) {
                        Set<Integer> inDbNotMem = new HashSet<>(dbSet);
                        inDbNotMem.removeAll(memSet);
                        Set<Integer> inMemNotDb = new HashSet<>(memSet);
                        inMemNotDb.removeAll(dbSet);
                        if (!inDbNotMem.isEmpty()) System.err.println("    In DB but not Memory: " + inDbNotMem);
                        if (!inMemNotDb.isEmpty()) System.err.println("    In Memory but not DB: " + inMemNotDb);
                    }
                }
            }

            // ── 2. Recompută balance din DB ──
            double dbBalance = db.loadTotalBalance();
            double memoryBalance = hall.getTotalBalance();
            boolean balanceMatch = Math.abs(dbBalance - memoryBalance) < 0.01;

            // ── 3. Cross-check: sold seats vs sales log ──
            List<Sale> sales = db.loadSales();
            Map<Integer, Integer> seatCountPerShow = new HashMap<>();
            double totalFromSales = 0.0;

            for (Sale sale : sales) {
                if (sale.getStatus() == SaleStatus.PAID) {
                    seatCountPerShow.merge(sale.getShowId(), sale.getNumTickets(), Integer::sum);
                    totalFromSales += sale.getTotalAmount();
                }
            }

            boolean salesConsistent = true;
            for (Map.Entry<Integer, Integer> entry : seatCountPerShow.entrySet()) {
                int showId = entry.getKey();
                int expectedSeats = entry.getValue();
                int actualSeats = memorySeats.getOrDefault(showId, new HashSet<>()).size();
                if (expectedSeats != actualSeats) {
                    salesConsistent = false;
                    System.err.println("[VERIFICATION] Mismatch for show " + showId +
                                     ": expected " + expectedSeats + " seats, found " + actualSeats);
                    break;
                }
            }

            // ── 4. Verifică că totalul din sales = balance ──
            boolean salesTotalMatch = Math.abs(totalFromSales - memoryBalance) < 0.01;

            boolean allCorrect = seatsMatch && balanceMatch && salesConsistent && salesTotalMatch;

            if (!allCorrect) {
                System.err.println("[VERIFICATION] Inconsistencies detected:");
                if (!seatsMatch) System.err.println("  - Seats mismatch between DB and memory");
                if (!balanceMatch) System.err.println("  - Balance mismatch: DB=" + dbBalance + ", memory=" + memoryBalance);
                if (!salesConsistent) System.err.println("  - Sales log inconsistent with sold seats");
                if (!salesTotalMatch) System.err.println("  - Sales total mismatch: sales=" + totalFromSales + ", balance=" + memoryBalance);
            }

            return new VerificationResult(
                LocalDateTime.now(),
                memorySeats,
                memoryBalance,
                sales,
                allCorrect ? "CORECT" : "INCORECT",
                seatsMatch,
                balanceMatch,
                salesConsistent
            );

        } catch (SQLException e) {
            System.err.println("[VERIFICATION] Database error: " + e.getMessage());
            return VerificationResult.error(e.getMessage());
        } finally {
            hall.getLock().unlock();
        }
    }
}
