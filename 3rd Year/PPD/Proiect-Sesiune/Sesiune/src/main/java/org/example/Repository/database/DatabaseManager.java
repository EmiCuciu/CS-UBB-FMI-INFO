package org.example.Repository.database;

import org.example.Model.Sale;
import org.example.Model.SaleStatus;
import org.example.Model.Show;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:shows.db";
    private Connection connection;
    private final ReentrantLock dbLock = new ReentrantLock();

    public DatabaseManager() throws SQLException {
        connection = DriverManager.getConnection(DB_URL);
        connection.setAutoCommit(false);
        System.out.println("Connected to SQLite database: shows.db");
        initializeSchema();
        insertTestData();
    }

    private void initializeSchema() throws SQLException {
        dbLock.lock();
        try {
            Statement stmt = connection.createStatement();

            // Tabel shows
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS shows (" +
                "    id INTEGER PRIMARY KEY," +
                "    date TEXT NOT NULL," +
                "    title TEXT NOT NULL," +
                "    price_per_ticket REAL NOT NULL" +
                ")"
            );

            // Tabel sales
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS sales (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    sale_date TEXT NOT NULL," +
                "    show_id INTEGER NOT NULL," +
                "    num_tickets INTEGER NOT NULL," +
                "    total_amount REAL NOT NULL," +
                "    status TEXT NOT NULL," +
                "    FOREIGN KEY (show_id) REFERENCES shows(id)" +
                ")"
            );

            // Tabel sold_seats
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS sold_seats (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    show_id INTEGER NOT NULL," +
                "    seat_number INTEGER NOT NULL," +
                "    sale_id INTEGER," +
                "    FOREIGN KEY (show_id) REFERENCES shows(id)," +
                "    FOREIGN KEY (sale_id) REFERENCES sales(id)," +
                "    UNIQUE(show_id, seat_number)" +
                ")"
            );

            // Index pentru performanță
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_sold_seats_show ON sold_seats(show_id)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_sales_show ON sales(show_id)");

            connection.commit();
            System.out.println("Database schema initialized.");
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            dbLock.unlock();
        }
    }

    private void insertTestData() throws SQLException {
        dbLock.lock();
        try {
            // Verifică dacă există deja date
            Statement checkStmt = connection.createStatement();
            ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) FROM shows");
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Test data already exists, skipping insertion.");
                return;
            }

            // Insert 3 spectacole conform specificațiilor
            PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO shows (id, date, title, price_per_ticket) VALUES (?, ?, ?, ?)"
            );

            // S1: pret 100
            stmt.setInt(1, 1);
            stmt.setString(2, "2026-02-15");
            stmt.setString(3, "Concert Rock");
            stmt.setDouble(4, 100.0);
            stmt.executeUpdate();

            // S2: pret 200
            stmt.setInt(1, 2);
            stmt.setString(2, "2026-02-20");
            stmt.setString(3, "Opera Clasică");
            stmt.setDouble(4, 200.0);
            stmt.executeUpdate();

            // S3: pret 150
            stmt.setInt(1, 3);
            stmt.setString(2, "2026-02-25");
            stmt.setString(3, "Spectacol Jazz");
            stmt.setDouble(4, 150.0);
            stmt.executeUpdate();

            connection.commit();
            System.out.println("Test data inserted: 3 shows (S1=100, S2=200, S3=150)");
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            dbLock.unlock();
        }
    }

    // ═══════════════════════════════════════════════════════════
    // LOAD OPERATIONS (la pornirea serverului)
    // ═══════════════════════════════════════════════════════════

    public List<Show> loadShows() throws SQLException {
        dbLock.lock();
        try {
            String sql = "SELECT * FROM shows";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<Show> shows = new ArrayList<>();
            while (rs.next()) {
                shows.add(new Show(
                    rs.getInt("id"),
                    rs.getString("date"),
                    rs.getString("title"),
                    rs.getDouble("price_per_ticket")
                ));
            }
            return shows;
        } finally {
            dbLock.unlock();
        }
    }

    public Map<Integer, Set<Integer>> loadSoldSeats() throws SQLException {
        dbLock.lock();
        try {
            String sql = "SELECT ss.show_id, ss.seat_number " +
                        "FROM sold_seats ss " +
                        "JOIN sales s ON ss.sale_id = s.id " +
                        "WHERE s.status = 'PAID'";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            Map<Integer, Set<Integer>> map = new HashMap<>();
            while (rs.next()) {
                int showId = rs.getInt("show_id");
                int seat = rs.getInt("seat_number");
                map.computeIfAbsent(showId, k -> new HashSet<>()).add(seat);
            }
            return map;
        } finally {
            dbLock.unlock();
        }
    }

    public List<Sale> loadSales() throws SQLException {
        dbLock.lock();
        try {
            String sql = "SELECT * FROM sales ORDER BY sale_date";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List<Sale> sales = new ArrayList<>();

            while (rs.next()) {
                int saleId = rs.getInt("id");
                int showId = rs.getInt("show_id");

                // Load seats for this sale
                List<Integer> seats = loadSeatsForSale(saleId, showId);

                sales.add(new Sale(
                    saleId,
                    rs.getString("sale_date"),
                    showId,
                    rs.getInt("num_tickets"),
                    seats,
                    rs.getDouble("total_amount"),
                    SaleStatus.valueOf(rs.getString("status"))
                ));
            }
            return sales;
        } finally {
            dbLock.unlock();
        }
    }

    private List<Integer> loadSeatsForSale(int saleId, int showId) throws SQLException {
        String sql = "SELECT seat_number FROM sold_seats WHERE sale_id = ? OR (show_id = ? AND sale_id IS NULL)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, saleId);
        stmt.setInt(2, showId);
        ResultSet rs = stmt.executeQuery();
        List<Integer> seats = new ArrayList<>();
        while (rs.next()) {
            seats.add(rs.getInt("seat_number"));
        }
        return seats;
    }

    public double loadTotalBalance() throws SQLException {
        dbLock.lock();
        try {
            String sql = "SELECT SUM(total_amount) FROM sales WHERE status = 'PAID'";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getDouble(1) : 0.0;
        } finally {
            dbLock.unlock();
        }
    }

    // ═══════════════════════════════════════════════════════════
    // WRITE OPERATIONS (pentru rezervări și plăți)
    // ═══════════════════════════════════════════════════════════

    public int insertReservation(int showId, List<Integer> seats) throws SQLException {
        dbLock.lock();
        try {
            // 1. Insert în sales cu status=RESERVED
            String sqlSale = "INSERT INTO sales (sale_date, show_id, num_tickets, total_amount, status) " +
                            "VALUES (?, ?, ?, 0.0, 'RESERVED')";
            PreparedStatement stmt = connection.prepareStatement(sqlSale, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, LocalDateTime.now().toString());
            stmt.setInt(2, showId);
            stmt.setInt(3, seats.size());
            stmt.executeUpdate();

            // Obtinem ID-ul generat
            ResultSet rs = stmt.getGeneratedKeys();
            int saleId = rs.next() ? rs.getInt(1) : -1;

            if (saleId == -1) {
                throw new SQLException("Failed to get generated sale ID");
            }

            // 2. Insert în sold_seats cu sale_id SETAT (pentru a putea identifica rezervarea)
            String sqlSeats = "INSERT INTO sold_seats (show_id, seat_number, sale_id) VALUES (?, ?, ?)";
            PreparedStatement stmtSeats = connection.prepareStatement(sqlSeats);
            for (int seat : seats) {
                stmtSeats.setInt(1, showId);
                stmtSeats.setInt(2, seat);
                stmtSeats.setInt(3, saleId);  // Setăm sale_id direct
                stmtSeats.addBatch();
            }
            stmtSeats.executeBatch();

            connection.commit();
            return saleId;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            dbLock.unlock();
        }
    }

    public void confirmPayment(int saleId, double amount) throws SQLException {
        dbLock.lock();
        try {
            // Update sales: status=PAID, total_amount
            String sqlUpdate = "UPDATE sales SET status='PAID', total_amount=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sqlUpdate);
            stmt.setDouble(1, amount);
            stmt.setInt(2, saleId);
            int updated = stmt.executeUpdate();

            if (updated == 0) {
                throw new SQLException("Sale ID not found: " + saleId);
            }

            // sold_seats rămâne neschimbat - sale_id e deja setat corect
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            dbLock.unlock();
        }
    }

    public void deleteExpiredReservation(int saleId) throws SQLException {
        dbLock.lock();
        try {
            // 1. Delete sold_seats pentru acest sale_id
            String sqlSeats = "DELETE FROM sold_seats WHERE sale_id=?";
            PreparedStatement stmt = connection.prepareStatement(sqlSeats);
            stmt.setInt(1, saleId);
            stmt.executeUpdate();

            // 2. Delete sale
            String sqlSale = "DELETE FROM sales WHERE id=?";
            PreparedStatement stmtSale = connection.prepareStatement(sqlSale);
            stmtSale.setInt(1, saleId);
            stmtSale.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            dbLock.unlock();
        }
    }

    // ═══════════════════════════════════════════════════════════
    // CLEANUP & CLOSE
    // ═══════════════════════════════════════════════════════════

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed.");
        }
    }

    public ReentrantLock getDbLock() {
        return dbLock;
    }
}
