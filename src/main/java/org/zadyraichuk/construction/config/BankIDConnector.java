package org.zadyraichuk.construction.config;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Configuration
public class BankIDConnector {

    private static final String BANK_ID_HOST = "id.bank.gov.ua";
    private static final String AUTHORIZATION_PATH = "/v1/bank/oauth2/authorize";
//    private static final String TOKEN_PATH = "/v1/bank/oauth2/authorize";

    private String clientId;
//    private String clientSecret;
//    private String authorizationKey;
//    private String accessToken;
    private URIBuilder uriBuilder;
    private HttpClient httpClient;

    public BankIDConnector(@Value("${bank.id.client.id}") String clientId,
                           @Value("${bank.id.client.secret}") String clientSecret) {
        this.clientId = clientId;
//        this.clientSecret = clientSecret;

        uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https");
        uriBuilder.setHost(BANK_ID_HOST);

        httpClient = HttpClient.newHttpClient();
    }

    public void doAuthorizationRequest(int sessionId) {
        resetBuilder(AUTHORIZATION_PATH);
        HttpRequest request;

        try {
            uriBuilder.addParameter("response_type", "code")
                    .addParameter("client_id", clientId)
                    .addParameter("state", String.valueOf(sessionId))
                    .addParameter("dataset", "11");

            request = HttpRequest.newBuilder()
                    .uri(uriBuilder.build())
                    .GET()
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new IllegalArgumentException("Error while building request");
        }
    }

    private void resetBuilder(String path) {
        uriBuilder.setPath(path);
        uriBuilder.clearParameters();
    }
}
