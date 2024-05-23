package org.zadyraichuk.construction.service.external;

import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.config.BankIDConnector;

@Service
public class BankIDService {

    BankIDConnector connector;

    public BankIDService(BankIDConnector connector) {
        this.connector = connector;
    }

    //TODO remove mockup
    public void redirectToAuthorization(int sessionId) {
//        connector.doAuthorizationRequest(sessionId);
    }

}
