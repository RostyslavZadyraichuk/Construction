package org.zadyraichuk.construction.service.external;

import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.config.YouControlConnector;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
public class YouControlService {

    YouControlConnector connector;

    public YouControlService(YouControlConnector connector) {
        this.connector = connector;
    }

    //TODO remove mockup
    public boolean isDirector(int companyIdentifier, String fullName) {
//        JSONObject company = findCompany(companyIdentifier);
//
//        if (company == null) {
//            return false;
//        }
//
//        String director = company.getString("director");
//        return fullName.toUpperCase().equals(director);
        return true;
    }

    //TODO create entity class for YouControl's company info
    private JSONObject findCompany(int companyIdentifier) {
        try {
            Optional<JSONObject> response = connector.getCompanyInfo(companyIdentifier);
            return response.orElse(null);
        } catch (HttpResponseException e) {
            return null;
        }
    }

}
