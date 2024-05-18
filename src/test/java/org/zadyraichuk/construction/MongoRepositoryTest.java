package org.zadyraichuk.construction;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.zadyraichuk.construction.entity.Subscription;
import org.zadyraichuk.construction.entity.User;
import org.zadyraichuk.construction.enumeration.UsageType;
import org.zadyraichuk.construction.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MongoRepositoryTest {

    @Autowired
    UserRepository ur;

    private static User user;

    @BeforeAll
    public static void create() {
        Subscription subscription = new Subscription("0123456789", UsageType.FREE, LocalDate.now(),
                LocalDate.now());

        user = new User();
        user.setSubscription(subscription);
        user.setEmail("test@test.com");
        user.setEncodedPassword("pass");
    }

    @Test
    @Order(1)
    void save() {
        user = ur.save(user);
        assertNotNull(user);
        assertNotNull(user.getId());
    }

    @Test
    @Order(2)
    void findById() {
        Optional<User> found = ur.findById(user.getId());
        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
    }

    @Test
    @Order(3)
    void findByEmail() {
        User found = ur.findByEmail(user.getEmail());
        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
    }

    @Test
    @Order(3)
    void findByFullName() {
        User found = ur.findByFullName(user.getFullName());
        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
    }

    @Test
    @Order(4)
    void update() {
        String oldEmail = user.getEmail();
        user.setEmail("new@test.com");
        ur.save(user);
        Optional<User> found = ur.findById(user.getId());
        assertTrue(found.isPresent());
        assertNotEquals(oldEmail, found.get().getEmail());
    }

    @Test
    @Order(5)
    void remove() {
        ur.delete(user);
        Optional<User> found = ur.findById(user.getId());
        assertFalse(found.isPresent());
    }

}