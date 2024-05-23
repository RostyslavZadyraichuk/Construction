package org.zadyraichuk.construction.config.external;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Configuration
public class GmailConnector {

    public static final int RECONNECT_COUNT = 5;
    public static final int RECONNECT_WAITING = 5000;

    private static final List<String> SCOPE = Arrays.asList(
            GmailScopes.GMAIL_COMPOSE,
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/userinfo.profile"
    );

    private Thread reconnectThread;

    private final LocalServerReceiver receiver;

    private Gmail client;

    public GmailConnector(@Value("${server.port}") int localServerPort) {
        receiver = new LocalServerReceiver.Builder().setPort(localServerPort).build();
        try {
            connectToGoogleOAuth();
        } catch (Exception e) {
            reconnect(5, 5000);
        }
    }

    public void reconnect(int checkingCount, int waitInMilliSeconds) {
        if (reconnectThread == null || reconnectThread.isInterrupted()) {
            try {
                reconnectThread = getReconnectThread(checkingCount, waitInMilliSeconds);
                reconnectThread.start();
            } catch (Exception ignored) {}
        }
    }

    public Optional<Gmail> getClientOpt() {
        return Optional.ofNullable(client);
    }

    private Thread getReconnectThread(int checkingCount, int waitInMilliSeconds) {
        return new Thread(() -> {
            int counter = 0;

            while (client == null && counter < checkingCount) {
                try {
                    Thread.sleep(waitInMilliSeconds);
                    connectToGoogleOAuth();
                } catch (Exception ignored) {}

                counter++;
            }
        });
    }

    private void connectToGoogleOAuth()
            throws NullPointerException, GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
        DataStoreFactory dsf = new FileDataStoreFactory(new File("src/main/resources/tokens"));

        Credential credential = getCredential(httpTransport, gsonFactory, dsf, receiver);
        if (credential == null) {
            throw new NullPointerException("Cannot connect to Google OAuth Service");
        }

        client = new Gmail.Builder(httpTransport, gsonFactory, credential)
                .setApplicationName("BuildICO Gmail Service")
                .build();
    }

    private Credential getCredential(HttpTransport transport,
                                     JsonFactory factory,
                                     DataStoreFactory dataStore,
                                     LocalServerReceiver receiver) {
        try (InputStream fis = new FileInputStream("src/main/resources/google_client_secret.json")) {

            GoogleClientSecrets secrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(),
                    new InputStreamReader(fis));
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    transport,
                    factory,
                    secrets,
                    SCOPE)
                    .setDataStoreFactory(dataStore)
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();

            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("admin");

        } catch (Exception e) {
            return null;
        }
    }
}
