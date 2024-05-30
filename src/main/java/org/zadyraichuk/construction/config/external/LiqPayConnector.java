package org.zadyraichuk.construction.config.external;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Configuration
public class LiqPayConnector {

    private static final String LIQPAY_HOST = "www.liqpay.ua";
    private static final String PAYMENT_PATH = "/api/request";

    private String liqPayPrivateKey;
    private String liqPayPublicKey;
    private URIBuilder uriBuilder;
    private HttpClient httpClient;

    public LiqPayConnector(@Value("${liqpay.private.key}") String liqPayPrivateKey,
                           @Value("${liqpay.public.key}") String liqPayPublicKey) {
        this.liqPayPrivateKey = liqPayPrivateKey;
        this.liqPayPublicKey = liqPayPublicKey;

        uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https");
        uriBuilder.setHost(LIQPAY_HOST);

        httpClient = HttpClient.newHttpClient();
    }

    public void doPaymentRequest(double amount, String accessType, String subscriptionId) {
        resetBuilder(PAYMENT_PATH);
        HttpRequest request;

        try {
            String paymentData = generatePaymentData(amount, accessType, subscriptionId);

            uriBuilder.addParameter("response_type", "code")
                    .addParameter("data", paymentData)
                    .addParameter("signature", generatePaymentSignature(paymentData));

            request = HttpRequest.newBuilder()
                    .uri(uriBuilder.build())
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new IllegalArgumentException("Error while building request");
        }
    }

    private String generatePaymentData(double amount, String accessType, String subscriptionId) {
        JSONObject data = new JSONObject()
            .put("public_key", liqPayPublicKey)
            .put("version", "3")
            .put("action", "subscribe")
            .put("amount", String.valueOf(amount))
            .put("currency", "UAH")
            .put("description", accessType)
            .put("order_id", subscriptionId);
        return Base64.getEncoder().encodeToString(data.toString().getBytes());
    }

    private String generatePaymentSignature(String paymentData) {
        String signature = liqPayPrivateKey + paymentData + liqPayPrivateKey;
        byte[] sha1 = DigestUtils.sha1(signature);
        return Base64.getEncoder().encodeToString(sha1);
    }

    private void resetBuilder(String path) {
        uriBuilder.setPath(path);
        uriBuilder.clearParameters();
    }
}
