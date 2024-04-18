package org.zadyraichuk.construction.dto;

import org.zadyraichuk.construction.enumeration.Periodicity;
import org.zadyraichuk.construction.enumeration.UsageType;

import java.time.LocalDate;

public class Subscription {

    private int id;
    private UsageType usageType;
    private Periodicity periodicity;
    private LocalDate expirationDate;
    private boolean isPaid;

    public Subscription(int id,
                        UsageType usageType,
                        Periodicity periodicity,
                        LocalDate expirationDate,
                        boolean isPaid) {
        this.id = id;
        this.usageType = usageType;
        this.periodicity = periodicity;
        this.expirationDate = expirationDate;
        this.isPaid = isPaid;
    }
}
