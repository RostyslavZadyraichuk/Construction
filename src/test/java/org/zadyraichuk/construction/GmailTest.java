package org.zadyraichuk.construction;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zadyraichuk.construction.service.google.GmailService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GmailTest {

    private static String message;
    private static String subject;
    //real user mail here
    private static String to;

    @Autowired
    private GmailService gmailService;

    @BeforeAll
    static void setUpBeforeClass() {
        message = "Message from test";
        subject = "Nothing";
        to = "rostyslav.zadyraichuk.kn.2021@lpnu.ua";
    }

    @Test
    void sendEmailWrongToTest() {
        String temp = to;
        to = "error";
        boolean isSent = gmailService.sendEmail(to, subject, message);
        assertFalse(isSent);
        to = temp;
    }

    @Test
    void sendEmailValidTest() {
        boolean isSent = gmailService.sendEmail(to, subject, message);
        assertTrue(isSent);
    }

}
