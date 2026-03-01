package org.example.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VerificationResult {
    private LocalDateTime timestamp;
    private Map<Integer, Set<Integer>> soldSeatsPerShow;
    private double totalBalance;
    private List<Sale> sales;
    private String status;
    private boolean seatsMatch;
    private boolean balanceMatch;
    private boolean salesConsistent;
    private String errorMessage;

    public VerificationResult(LocalDateTime timestamp, Map<Integer, Set<Integer>> soldSeatsPerShow,
                              double totalBalance, List<Sale> sales, String status,
                              boolean seatsMatch, boolean balanceMatch, boolean salesConsistent) {
        this.timestamp = timestamp;
        this.soldSeatsPerShow = soldSeatsPerShow;
        this.totalBalance = totalBalance;
        this.sales = sales;
        this.status = status;
        this.seatsMatch = seatsMatch;
        this.balanceMatch = balanceMatch;
        this.salesConsistent = salesConsistent;
    }

    public static VerificationResult error(String errorMessage) {
        VerificationResult result = new VerificationResult(
            LocalDateTime.now(), null, 0.0, null, "ERROR", false, false, false
        );
        result.errorMessage = errorMessage;
        return result;
    }

    public String toLogString() {
        if (errorMessage != null) {
            return String.format("%s | ERROR: %s",
                timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                errorMessage);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sb.append(" | ");

        for (Map.Entry<Integer, Set<Integer>> entry : soldSeatsPerShow.entrySet()) {
            int showId = entry.getKey();
            int numSeats = entry.getValue().size();
            double showBalance = sales.stream()
                .filter(s -> s.getShowId() == showId && s.getStatus() == SaleStatus.PAID)
                .mapToDouble(Sale::getTotalAmount)
                .sum();
            sb.append(String.format("S%d: %d locuri (balance=%.2f) | ", showId, numSeats, showBalance));
        }

        sb.append(String.format("Total balance=%.2f | ", totalBalance));
        sb.append(String.format("Status: %s | ", status));
        sb.append(String.format("[seats=%s, balance=%s, sales=%s]",
            seatsMatch ? "OK" : "FAIL",
            balanceMatch ? "OK" : "FAIL",
            salesConsistent ? "OK" : "FAIL"));

        return sb.toString();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<Integer, Set<Integer>> getSoldSeatsPerShow() {
        return soldSeatsPerShow;
    }

    public double getTotalBalance() {
        return totalBalance;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public String getStatus() {
        return status;
    }

    public boolean isSeatsMatch() {
        return seatsMatch;
    }

    public boolean isBalanceMatch() {
        return balanceMatch;
    }

    public boolean isSalesConsistent() {
        return salesConsistent;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
