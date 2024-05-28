package org.zadyraichuk.construction.dto;

import lombok.Getter;
import org.zadyraichuk.construction.enumeration.Periodicity;
import org.zadyraichuk.construction.enumeration.UsageType;

import java.time.LocalDate;

@Getter
public class SubscriptionDTO {

    private String id;
    private UsageType usageType;
    private Periodicity periodicity;
    private LocalDate expirationDate;

    public SubscriptionDTO(String id,
                           UsageType usageType,
                           Periodicity periodicity,
                           LocalDate expirationDate) {
        this.id = id;
        this.usageType = usageType;
        this.periodicity = periodicity;
        this.expirationDate = expirationDate;
    }

    public boolean isPaid() {
        return expirationDate.isBefore(LocalDate.now());
    }
}
