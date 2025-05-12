package triatlon.persistence;

import triatlon.model.Arbitru;
import triatlon.model.Participant;
import triatlon.model.Rezultat;
import triatlon.model.TipProba;
import triatlon.persistence.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RezultatRepository implements IRezultatRepository {

    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils jdbcUtils;
    private final IParticipantRepository participantRepository;
    private final IArbitruRepository arbitruRepository;

    public RezultatRepository(Properties properties,
                              IParticipantRepository participantRepository,
                              IArbitruRepository arbitruRepository) {
        logger.info("Initializing RezultatRepository with properties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
        this.participantRepository = participantRepository;
        this.arbitruRepository = arbitruRepository;
    }

    @Override
    public Rezultat findOne(Integer integer) {
        logger.traceEntry("finding Rezultat with id {}", integer);
        String sql = "SELECT * FROM Rezultat WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, integer);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int participantId = rs.getInt("participant_id");
                    int arbitruId = rs.getInt("arbitru_id");
                    String tipProbaStr = rs.getString("tip_proba");
                    int punctaj = rs.getInt("punctaj");

                    Participant participant = participantRepository.findOne(participantId);
                    Arbitru arbitru = arbitruRepository.findOne(arbitruId);
                    TipProba tipProba = TipProba.valueOf(tipProbaStr);

                    return new Rezultat(
                            rs.getInt("id"),
                            participant,
                            arbitru,
                            tipProba,
                            punctaj
                    );
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Iterable<Rezultat> findAll() {
        List<Rezultat> rezultate = new ArrayList<>();
        logger.traceEntry("finding all Rezultate");
        String sql = "SELECT * FROM Rezultat";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int participantId = resultSet.getInt("participant_id");
                int arbitruId = resultSet.getInt("arbitru_id");
                String tipProbaStr = resultSet.getString("tip_proba");
                int punctaj = resultSet.getInt("punctaj");

                Participant participant = participantRepository.findOne(participantId);
                Arbitru arbitru = arbitruRepository.findOne(arbitruId);
                TipProba tipProba = TipProba.valueOf(tipProbaStr);

                rezultate.add(new Rezultat(
                        resultSet.getInt("id"),
                        participant,
                        arbitru,
                        tipProba,
                        punctaj
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
        return rezultate;
    }

    @Override
    public void save(Rezultat entity) {
        logger.traceEntry("saving Rezultat {}", entity);
        String sql = "INSERT INTO Rezultat (participant_id, arbitru_id, tip_proba, punctaj) VALUES (?, ?, ?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, entity.getParticipant().getId());
            preparedStatement.setInt(2, entity.getArbitru().getId());
            preparedStatement.setString(3, entity.getTipProba().name());
            preparedStatement.setInt(4, entity.getPunctaj());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Rezultat with id {}", integer);
        String sql = "DELETE FROM Rezultat WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Rezultat entity) {
        logger.traceEntry("updating Rezultat {}", entity);
        String sql = "UPDATE Rezultat SET participant_id = ?, arbitru_id = ?, tip_proba = ?, punctaj = ? WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, entity.getParticipant().getId());
            preparedStatement.setInt(2, entity.getArbitru().getId());
            preparedStatement.setString(3, entity.getTipProba().name());
            preparedStatement.setInt(4, entity.getPunctaj());
            preparedStatement.setInt(5, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }
}