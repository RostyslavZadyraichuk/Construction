package org.zadyraichuk.construction.service.external;

import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.config.LiqPayConnector;

@Service
public class LiqPayService {

    public LiqPayConnector connector;

    public LiqPayService(LiqPayConnector connector) {
        this.connector = connector;
    }

    //TODO remove mockup
    public void redirectToPay(double amount, String accessType, String subscriptionId) {
//        connector.doPaymentRequest(amount, accessType, subscriptionId);
    }
}
