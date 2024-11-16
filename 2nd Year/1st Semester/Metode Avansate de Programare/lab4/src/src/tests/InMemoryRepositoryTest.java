package src.tests;

import org.junit.jupiter.api.Test;
import src.domain.User;
import src.domain.validators.ValidatorUser;
import src.repository.memory.InMemoryRepository;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryRepositoryTest {

    @Test
    public void testAddAndFind() {
        InMemoryRepository<Long, User> userRepo = new InMemoryRepository<>(new ValidatorUser());
        User user = new User("John", "Doe");
        user.setId(1L);
        userRepo.save(user);

        assertTrue(userRepo.findOne(1L).isPresent(), "User should be present in the repository");
    }

    @Test
    public void testDelete() {
        InMemoryRepository<Long, User> userRepo = new InMemoryRepository<>(new ValidatorUser());
        User user = new User("Jane", "Doe");
        user.setId(2L);
        userRepo.save(user);

        userRepo.delete(2L);
        assertFalse(userRepo.findOne(2L).isPresent(), "User should be removed from the repository");
    }

    @Test
    public void testUpdate() {
        InMemoryRepository<Long, User> userRepo = new InMemoryRepository<>(new ValidatorUser());
        User user = new User("John", "Smith");
        user.setId(3L);
        userRepo.save(user);

        User updatedUser = new User("John", "Doe");
        updatedUser.setId(3L);
        userRepo.update(updatedUser);

        assertEquals("Doe", userRepo.findOne(3L).get().getLastName(), "User last name should be updated to Doe");
    }
}
