package org.zadyraichuk.construction.service.external;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.config.external.GmailConnector;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.zadyraichuk.construction.config.external.GmailConnector.*;

@Service
public class GmailService {

    private final GmailConnector connector;

    private static final String SENDER = "me";

    public GmailService(GmailConnector connector) {
        this.connector = connector;
    }

    public boolean sendEmail(String toEmail, String subject, String message) {
        Optional<Gmail> clientOpt = connector.getClientOpt();

        if (clientOpt.isPresent()) {
            try {
                MimeMessage mime = generateMimeMessage(toEmail, subject, message);
                Message gmailMessage = generateGmailMessage(mime);
                gmailMessage = clientOpt.get().users().messages().send(SENDER, gmailMessage).execute();

                if (gmailMessage != null && gmailMessage.getId() != null) {
                    return true;
                }
            } catch (MessagingException | IOException e) {
                return false;
            }
        }

        connector.reconnect(RECONNECT_COUNT, RECONNECT_WAITING);
        return false;
    }

    private MimeMessage generateMimeMessage(String toEmail, String subject, String message)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(SENDER));
        email.addRecipient(RecipientType.TO, new InternetAddress(toEmail));
        email.setSubject(subject);
        email.setText(message);
        return email;
    }

    private Message generateGmailMessage(MimeMessage email) throws IOException, MessagingException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

}
