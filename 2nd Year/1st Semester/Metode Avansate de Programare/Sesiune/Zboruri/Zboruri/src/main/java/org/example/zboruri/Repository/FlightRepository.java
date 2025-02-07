package org.example.zboruri.Repository;

import org.example.zboruri.Domain.Flight;
import org.example.zboruri.Utils.Paging.Page;
import org.example.zboruri.Utils.Paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightRepository implements PagingRepository<Long, Flight> {
    @Override
    public Flight findOne(Long id) {
        String sql = "SELECT * FROM flights WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Flight(
                        resultSet.getLong("id"),
                        resultSet.getString("departure_location"),
                        resultSet.getString("arrival_location"),
                        resultSet.getTimestamp("departure_time").toLocalDateTime(),
                        resultSet.getTimestamp("landing_time").toLocalDateTime(),
                        resultSet.getInt("seats")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM flights");
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Flight flight = new Flight(
                        resultSet.getLong("id"),
                        resultSet.getString("departure_location"),
                        resultSet.getString("arrival_location"),
                        resultSet.getTimestamp("departure_time").toLocalDateTime(),
                        resultSet.getTimestamp("landing_time").toLocalDateTime(),
                        resultSet.getInt("seats")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return flights;
    }

    public List<String> findAllLocations() {
        List<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT departure_location FROM flights UNION SELECT DISTINCT arrival_location FROM flights";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                locations.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return locations;
    }

    public List<Flight> findByDateAndLocations(LocalDateTime date, String from, String to) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE CAST(departure_time AS DATE) = ? AND departure_location = ? AND arrival_location = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));
            statement.setString(2, from);
            statement.setString(3, to);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Flight flight = new Flight(
                        resultSet.getLong("id"),
                        resultSet.getString("departure_location"),
                        resultSet.getString("arrival_location"),
                        resultSet.getTimestamp("departure_time").toLocalDateTime(),
                        resultSet.getTimestamp("landing_time").toLocalDateTime(),
                        resultSet.getInt("seats")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Found flights: " + flights.size());
        return flights;
    }

    @Override
    public Flight save(Flight entity) {
        String sql = "INSERT INTO flights (departure_location, arrival_location, departure_time, landing_time, seats) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFrom());
            statement.setString(2, entity.getTo());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDepartureTime()));
            statement.setTimestamp(4, Timestamp.valueOf(entity.getLandingTime()));
            statement.setInt(5, entity.getSeats());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                entity.setId(rs.getLong(1));
            }
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Flight delete(Long id) {
        Flight flight = findOne(id);
        if (flight != null) {
            String sql = "DELETE FROM flights WHERE id = ?";
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return flight;
    }

    @Override
    public Flight update(Flight entity) {
        String sql = "UPDATE flights SET departure_location = ?, arrival_location = ?, departure_time = ?, landing_time = ?, seats = ? WHERE id = ?";
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, entity.getFrom());
            statement.setString(2, entity.getTo());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getDepartureTime()));
            statement.setTimestamp(4, Timestamp.valueOf(entity.getLandingTime()));
            statement.setInt(5, entity.getSeats());
            statement.setLong(6, entity.getId());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Flight> findAllOnPage(Pageable pageable) {
        List<Flight> flights = new ArrayList<>();
        int totalCount = 0;

        String countSql = "SELECT COUNT(*) FROM flights";
        String pageSql = "SELECT * FROM flights ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection connection = DataBaseConnection.getConnection()) {
            // Get total count
            try (PreparedStatement countStatement = connection.prepareStatement(countSql)) {
                ResultSet countResult = countStatement.executeQuery();
                if (countResult.next()) {
                    totalCount = countResult.getInt(1);
                }
            }

            // Get page data
            try (PreparedStatement pageStatement = connection.prepareStatement(pageSql)) {
                pageStatement.setInt(1, pageable.getPageNumber() * pageable.getPageSize());
                pageStatement.setInt(2, pageable.getPageSize());
                ResultSet resultSet = pageStatement.executeQuery();

                while (resultSet.next()) {
                    Flight flight = new Flight(
                            resultSet.getLong("id"),
                            resultSet.getString("departure_location"),
                            resultSet.getString("arrival_location"),
                            resultSet.getTimestamp("departure_time").toLocalDateTime(),
                            resultSet.getTimestamp("landing_time").toLocalDateTime(),
                            resultSet.getInt("seats")
                    );
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Page<>(flights, totalCount);
    }

    public Page<Flight> findFilteredFlightsOnPage(LocalDateTime date, String from, String to, Pageable pageable) {
        List<Flight> flights = new ArrayList<>();
        int totalCount = 0;

        String countSql = "SELECT COUNT(*) FROM flights WHERE CAST(departure_time AS DATE) = ? AND departure_location = ? AND arrival_location = ?";
        String pageSql = "SELECT * FROM flights WHERE CAST(departure_time AS DATE) = ? AND departure_location = ? AND arrival_location = ? ORDER BY id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection connection = DataBaseConnection.getConnection()) {
            // Get total count
            try (PreparedStatement countStatement = connection.prepareStatement(countSql)) {
                countStatement.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));
                countStatement.setString(2, from);
                countStatement.setString(3, to);
                ResultSet countResult = countStatement.executeQuery();
                if (countResult.next()) {
                    totalCount = countResult.getInt(1);
                }
            }

            // Get page data
            try (PreparedStatement pageStatement = connection.prepareStatement(pageSql)) {
                pageStatement.setDate(1, java.sql.Date.valueOf(date.toLocalDate()));
                pageStatement.setString(2, from);
                pageStatement.setString(3, to);
                pageStatement.setInt(4, pageable.getPageNumber() * pageable.getPageSize());
                pageStatement.setInt(5, pageable.getPageSize());
                ResultSet resultSet = pageStatement.executeQuery();

                while (resultSet.next()) {
                    Flight flight = new Flight(
                            resultSet.getLong("id"),
                            resultSet.getString("departure_location"),
                            resultSet.getString("arrival_location"),
                            resultSet.getTimestamp("departure_time").toLocalDateTime(),
                            resultSet.getTimestamp("landing_time").toLocalDateTime(),
                            resultSet.getInt("seats")
                    );
                    flights.add(flight);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Page<>(flights, totalCount);
    }

}