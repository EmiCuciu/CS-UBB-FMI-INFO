package chat.persistence.repository.jdbc;

import chat.model.User;
import chat.persistence.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;


public class UserRepositoryJdbc implements UserRepository {
    private JdbcUtils jdbcUtils;
    private static Logger logger = LogManager.getLogger(UserRepositoryJdbc.class);

    public UserRepositoryJdbc(Properties jdbcProps){
        jdbcUtils=new JdbcUtils(jdbcProps);
    }

    @Override
    public User findBy(String username) {
        return null;
    }

    @Override
    public User findBy(String username, String pass) {
        logger.debug ("JDBC findBy 2 params: user={}, pass={}", username, pass);
        Connection con=jdbcUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select name from users where username=? and password=?")){
            preStmt.setString(1,username);
            preStmt.setString(2,EncryptUtils.encrypt(pass,EncryptUtils.secretKeyChat));
            ResultSet result=preStmt.executeQuery();
            boolean resOk=result.next();
            logger.debug("findBy user, pass "+resOk);
            if (resOk) {
                User user=new User(username);
                user.setName(result.getString("name"));
                return logger.traceExit(user);
            }
        } catch (SQLException e) {
            logger.error("Error DB "+e);
        }
        return null;
    }

    @Override
    public Iterable<User> getFriendsOf(User user) {
        logger.debug("JDBC get friends of {}", user);
        Connection con=jdbcUtils.getConnection();
        ArrayList<User> result=new ArrayList<User>();
        try (PreparedStatement preStmt=con.prepareStatement("select friends.userA, friends.userB  from friends where  friends.userA=? or friends.userB=?")){
             preStmt.setString(1,user.getId());
            preStmt.setString(2,user.getId());
            ResultSet rs=preStmt.executeQuery();
            while(rs.next()){
                String friendA=rs.getString(1);
                String friendB=rs.getString(2);
                User u=new User(friendA.equals(user.getId())?friendB:friendA);
                result.add(u);
            }
            logger.debug("List of friends for user={} is {}", user,result);
        } catch (SQLException e) {
            logger.error("Error DB "+e);
        }
        return result;
    }

    @Override
    public void save(User user) { }

    @Override
    public void delete(String s) { }

    @Override
    public User findOne(String s) {
        return null;
    }

    @Override
    public void update(String s, User user) { }

    @Override
    public Iterable<User> getAll() {
        return null;
    }
}
