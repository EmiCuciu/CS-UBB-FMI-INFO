package chat.server;

import chat.model.Message;
import chat.model.User;
import chat.persistence.MessageRepository;
import chat.persistence.UserRepository;
import chat.services.ChatException;
import chat.services.IChatObserver;
import chat.services.IChatServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChatServicesImpl implements IChatServices {

    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private Map<String, IChatObserver> loggedClients;
    private static Logger logger = LogManager.getLogger(ChatServicesImpl.class);

    public ChatServicesImpl(UserRepository uRepo, MessageRepository mRepo) {
        userRepository= uRepo;
        messageRepository=mRepo;
        loggedClients=new ConcurrentHashMap<>();;
    }


    public synchronized void login(User user, IChatObserver client) throws ChatException {
        User userR=userRepository.findBy(user.getId(),user.getPasswd());
        if (userR!=null){
            if(loggedClients.get(user.getId())!=null)
                throw new ChatException("User already logged in.");
            loggedClients.put(user.getId(), client);
            notifyFriendsLoggedIn(user);
        }else
            throw new ChatException("Authentication failed.");
    }


    private final int defaultThreadsNo=3;
    private void notifyFriendsLoggedIn(User user) throws ChatException {
        Iterable<User> friends=userRepository.getFriendsOf(user);
        logger.debug("Logged "+friends);

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(User us :friends){
            IChatObserver chatClient=loggedClients.get(us.getId());
            if (chatClient!=null)
                executor.execute(() -> {
                    try {
                        logger.debug("Notifying [" + us.getId()+ "] friend ["+user.getId()+"] logged in.");
                        chatClient.friendLoggedIn(user);
                    } catch (ChatException e) {
                        logger.error("Error notifying friend {} {}", user.getId(), e);
                    }
                });
        }

        executor.shutdown();
    }

    private void notifyFriendsLoggedOut(User user) throws ChatException {
        Iterable<User> friends=userRepository.getFriendsOf(user);
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(User us :friends){
            IChatObserver chatClient=loggedClients.get(us.getId());
            if (chatClient!=null)
                executor.execute(() -> {
                    try {
                        logger.debug("Notifying ["+us.getId()+"] friend ["+user.getId()+"] logged out.");
                        chatClient.friendLoggedOut(user);
                    } catch (ChatException e) {
                        logger.error("Error notifying friend " + e);
                    }
                });

        }
        executor.shutdown();
    }

    public synchronized void sendMessage(Message message) throws ChatException {
        String id_receiver=message.getReceiver().getId();
        IChatObserver receiverClient=loggedClients.get(id_receiver);
        if (receiverClient!=null) {      //the receiver is logged in
            messageRepository.save(message);
            receiverClient.messageReceived(message);
        }
        else
            throw new ChatException("User "+id_receiver+" not logged in.");
    }

    public synchronized void logout(User user, IChatObserver client) throws ChatException {
        IChatObserver localClient=loggedClients.remove(user.getId());
        if (localClient==null)
            throw new ChatException("User "+user.getId()+" is not logged in.");
        notifyFriendsLoggedOut(user);
    }

    public synchronized User[] getLoggedFriends(User user) throws ChatException {
       Iterable<User> friends=userRepository.getFriendsOf(user);
        Set<User> result=new TreeSet<User>();
        logger.debug("Logged friends for: "+user.getId());
        for (User friend : friends){
            if (loggedClients.containsKey(friend.getId())){    //the friend is logged in
                result.add(new User(friend.getId()));
                logger.debug("+"+friend.getId());
            }
        }
        logger.debug("Size "+result.size());
        return result.toArray(new User[result.size()]);
    }
}
