package org.example.lab6.repository.database;

import org.example.lab6.domain.Friendship;
import org.example.lab6.domain.Tuple;
import org.example.lab6.domain.validation.Validator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipDBRepository extends AbstractDBRepository<Tuple<Long, Long>, Friendship> {

    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        super(url, username, password, validator);
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> longLongTuple) {
        String FIND_ONE_QUERY = "select * from friendship where id1=? and id2=?";
        try (Connection connection = prepareConcection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ONE_QUERY)) {
            preparedStatement.setLong(1, longLongTuple.getE1());
            preparedStatement.setLong(2, longLongTuple.getE2());
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(mapToFriendship(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Friendship> findAll() {
        List<Friendship> friendships = new ArrayList<>();
        String FIND_ALL_QUERY = "select * from friendship";
        try (Connection connection = prepareConcection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
             while (resultSet.next()){
                 friendships.add(mapToFriendship(resultSet));
             }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships;
    }

    @Override
    public Optional<Friendship> save(Friendship friendship) {
        validator.validate(friendship);
        String SAVE_QUERY = "insert into friendship (id1, id2, Date, User1, User2) values(?,?,?,?,?)";
        try (Connection connection = prepareConcection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_QUERY)) {
            preparedStatement.setLong(1, friendship.getId().getE1());
            preparedStatement.setLong(2, friendship.getId().getE2());
            preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(friendship.getDate()));
            preparedStatement.setString(4, friendship.getUser1());
            preparedStatement.setString(5, friendship.getUser2());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(friendship);
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> longLongTuple) {
        Optional<Friendship> friendship = findOne(longLongTuple);
        if (friendship.isEmpty())
            return Optional.empty();

        String DELETE_QUERY = "DELETE FROM friendship WHERE id1 = ? AND id2 = ?";
        try (Connection connection = prepareConcection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setLong(1, longLongTuple.getE1());
            preparedStatement.setLong(2, longLongTuple.getE2());
            preparedStatement.executeUpdate();
            return friendship;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship friendship){
        return Optional.empty();
    }

    private Friendship mapToFriendship(ResultSet resultSet) throws SQLException {
        Tuple<Long, Long> id = new Tuple<>(resultSet.getLong("id1"), resultSet.getLong("id2"));
        LocalDateTime requestDate = resultSet.getTimestamp("Date").toLocalDateTime();
        String user1 = resultSet.getString("User1");
        String user2 = resultSet.getString("User2");
        Friendship friendship = new Friendship(requestDate, user1, user2);
        friendship.setId(id);


        return friendship;
    }
}
