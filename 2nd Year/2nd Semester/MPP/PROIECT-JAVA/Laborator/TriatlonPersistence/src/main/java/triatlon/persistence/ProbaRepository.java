package triatlon.persistence;

import triatlon.model.Arbitru;
import triatlon.model.Proba;
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


public class ProbaRepository implements IProbaRepository {

    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils jdbcUtils;
    private final IArbitruRepository arbitruRepository;

    public ProbaRepository(Properties properties, IArbitruRepository arbitruRepository) {
        logger.info("Initializing ProbaRepository with proprieties: {}", properties);
        jdbcUtils = new JdbcUtils(properties);
        this.arbitruRepository = arbitruRepository;
    }

    @Override
    public Proba findOne(Integer integer) {
        logger.traceEntry("finding Proba with id {}", integer);
        String sql = "SELECT * FROM Proba WHERE id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, integer);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int arbitruId = rs.getInt("arbitru_id");
                    Arbitru arbitru = arbitruRepository.findOne(arbitruId);

                    String tipProbaStr = rs.getString("tip_proba");
                    TipProba tipProba = TipProba.valueOf(tipProbaStr);

                    return new Proba(
                            rs.getInt("id"),
                            tipProba,
                            arbitru
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
    public Iterable<Proba> findAll() {
        List<Proba> probe = new ArrayList<>();
        logger.traceEntry("finding all Probe");
        String sql = "SELECT * FROM Proba";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int arbitruId = resultSet.getInt("arbitru_id");
                Arbitru arbitru = arbitruRepository.findOne(arbitruId);

                String tipProbaStr = resultSet.getString("tip_proba");
                TipProba tipProba = TipProba.valueOf(tipProbaStr);

                probe.add(new Proba(
                        resultSet.getInt("id"),
                        tipProba,
                        arbitru
                ));
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
        return probe;
    }

    @Override
    public void save(Proba entity) {
        logger.traceEntry("saving Proba {}", entity);
        String sql = "insert into Proba (tip_proba, arbitru_id) values (?, ?)";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, entity.getTipProba().name());
            preparedStatement.setInt(2, entity.getArbitru().getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generateKeys = preparedStatement.getGeneratedKeys()) {
                    if (generateKeys.next()) {
                        entity.setId(generateKeys.getInt(1));
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
        logger.traceEntry("delete Proba with id {}", integer);
        String sql = "delete from Proba where id = ?";
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
    public void update(Proba entity) {
        logger.traceEntry("update Proba {}", entity);
        String sql = "update Proba set tip_proba = ?, arbitru_id = ? where id = ?";

        try (Connection connection = jdbcUtils.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getTipProba().name());
            preparedStatement.setInt(2, entity.getArbitru().getId());
            preparedStatement.setInt(3, entity.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("SQLException: " + e.getMessage());
        }
        logger.traceExit();
    }
}
