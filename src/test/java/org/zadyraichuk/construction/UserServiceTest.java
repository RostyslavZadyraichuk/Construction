package org.zadyraichuk.construction;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.zadyraichuk.construction.dto.SubscriptionDTO;
import org.zadyraichuk.construction.dto.UserDTO;
import org.zadyraichuk.construction.dto.creation.RegisterUserDTO;
import org.zadyraichuk.construction.enumeration.Periodicity;
import org.zadyraichuk.construction.enumeration.Role;
import org.zadyraichuk.construction.enumeration.UsageType;
import org.zadyraichuk.construction.service.UserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(excludeAutoConfiguration = {
        SecurityAutoConfiguration.class
})
@ComponentScan(basePackages = {
        "org.zadyraichuk.construction.service.impl",
        "org.zadyraichuk.construction.service.mapper",
        "org.zadyraichuk.construction.config",
        "org.zadyraichuk.construction.repository"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Autowired
    private UserService us;

    private static SubscriptionDTO subscription = new SubscriptionDTO("3456789", UsageType.FREE,
            Periodicity.MONTHLY, LocalDate.now().plusDays(30));
    private static UserDTO user;
    private static String email = "test@test.com";

    @BeforeAll
    void setUpBeforeAll() {
        RegisterUserDTO register = new RegisterUserDTO("test_user", "pass", "pass", email, Role.GENERAL_CONTRACTOR);
        user = us.save(register).orElse(null);
        assert user != null;
        user.setSubscription(subscription);
    }

    @Test
    @Order(1)
    void saveTest() {
        assertNotNull(user);
        assertNotEquals(0, user.getUserId());
        System.out.println(user);
    }

    @Test
    @Order(2)
    void findByIdTest() {
        Optional<UserDTO> found = us.findById(user.getUserId());
        assertTrue(found.isPresent());
        assertEquals(user.getUserId(), found.get().getUserId());
        System.out.println(found.get());
    }

    @Test
    @Order(3)
    void findByEmailTest() {
        Optional<UserDTO> found = us.findByEmail(email);
        assertTrue(found.isPresent());
        assertEquals(user.getUserId(), found.get().getUserId());
        System.out.println(found.get());
    }

    @Test
    @Order(4)
    void findByUsernameTest() {
        Optional<UserDTO> found = us.findByUsername(user.getUserName());
        assertTrue(found.isPresent());
        assertEquals(user.getUserId(), found.get().getUserId());
        System.out.println(found.get());
    }

    @Test
    @Order(5)
    void updateTest() {
        String oldUsername = user.getUserName();
        user.setUserName("newUsername");
        Optional<UserDTO> updated = us.update(user);
        assertTrue(updated.isPresent());
        assertEquals(user.getUserId(), updated.get().getUserId());
        assertNotEquals(oldUsername, updated.get().getUserName());
        System.out.println(updated.get());
    }

    @Test
    @Order(6)
    void removeTest() {
        boolean isDeleted = us.delete(user);
        Optional<UserDTO> found = us.findById(user.getUserId());
        assertFalse(found.isPresent());
        assertTrue(isDeleted);
    }

}