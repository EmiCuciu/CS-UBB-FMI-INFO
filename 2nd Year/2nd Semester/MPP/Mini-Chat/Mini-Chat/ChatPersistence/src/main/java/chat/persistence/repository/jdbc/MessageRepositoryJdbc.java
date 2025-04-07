package chat.persistence.repository.jdbc;

import chat.model.Message;
import chat.persistence.MessageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

public class MessageRepositoryJdbc implements MessageRepository {
    private JdbcUtils jdbcUtils;
    private static Logger logger = LogManager.getLogger(MessageRepositoryJdbc.class);
    public MessageRepositoryJdbc(Properties props){
        jdbcUtils=new JdbcUtils(props);
    }
    @Override
    public void save(Message message) {
        logger.debug("saving message {}", message);
        Connection con=jdbcUtils.getConnection();
        try (PreparedStatement preStmt=con.prepareStatement("insert into messages (sender,receiver,mtext, mdata) values (?,?,?,?)")){

            preStmt.setString(1,message.getSender().getId());
            preStmt.setString(2,message.getReceiver().getId());
            preStmt.setString(3,message.getText());
            preStmt.setString(4, LocalDate.now().toString());
            if (preStmt.executeUpdate()<1)
                logger.error("Message not saved");
        } catch (SQLException e) {
            logger.error("Error Jdbc "+e);
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public Message findOne(Integer integer) {
        return null;
    }

    @Override
    public void update(Integer integer, Message message) {

    }

    @Override
    public Iterable<Message> getAll() {
        return null;
    }
}
