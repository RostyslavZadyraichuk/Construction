package org.zadyraichuk.construction.config.external;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.apache.http.HttpStatus.*;

@Configuration
public class YouControlConnector {

    private static final String YOU_CONTROL_HOST = "api.youscore.com.ua";
    private static final String COMPANY_INFO_PATH = "/v1/companyInfo/";

    private String youControlPublicKey;
    private URIBuilder uriBuilder;
    private HttpClient httpClient;

    public YouControlConnector(@Value("${you.control.public.key}") String youControlPublicKey) {
        this.youControlPublicKey = youControlPublicKey;

        uriBuilder = new URIBuilder();
        uriBuilder.setScheme("https");
        uriBuilder.setHost(YOU_CONTROL_HOST);

        httpClient = HttpClient.newHttpClient();
    }

    public Optional<JSONObject> getCompanyInfo(int companyIdentifier) throws HttpResponseException {
        JSONObject companyInfo = null;
        Optional<HttpResponse<String>> responseOptional = doCompanyInfoRequest(companyIdentifier);

        if (responseOptional.isPresent()) {
            HttpResponse<String> response = responseOptional.get();

            switch (response.statusCode()) {
                case SC_OK:
                case SC_CREATED:
                case SC_ACCEPTED:
                    companyInfo = new JSONObject(response.body());
                    break;
                case SC_NOT_FOUND:
                    throw new HttpResponseException(SC_NOT_FOUND, "Company not found");
                case SC_BAD_REQUEST:
                default:
                    throw new HttpResponseException(SC_BAD_REQUEST, "Exception during request");
            }
        }

        return Optional.ofNullable(companyInfo);
    }

    private Optional<HttpResponse<String>> doCompanyInfoRequest(int companyIdentifier) {
        resetBuilder(COMPANY_INFO_PATH + companyIdentifier);
        HttpRequest request;
        HttpResponse<String> response;

        try {
            request = HttpRequest.newBuilder()
                    .uri(uriBuilder.build())
                    .GET()
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new IllegalArgumentException("Error while building request");
        }
        return Optional.ofNullable(response);
    }

    private void resetBuilder(String path) {
        uriBuilder.setPath(path);
        uriBuilder.clearParameters();
        uriBuilder.addParameter("apiKey", youControlPublicKey);
    }

}
