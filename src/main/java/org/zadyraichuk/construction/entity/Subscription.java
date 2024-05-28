package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.zadyraichuk.construction.enumeration.Periodicity;
import org.zadyraichuk.construction.enumeration.UsageType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class Subscription {

    @Field(name = "operation_id")
    @Indexed(unique = true)
    @NotNull
    private String operationId;

    @Field(name = "usage_type")
    @NotNull
    private UsageType usageType;

    @Field(name = "payment_date")
    @NotNull
    private LocalDate paymentDate;

    @Field(name = "expiration_date")
    @NotNull
    private LocalDate expirationDate;

    @Field(name = "periodicity")
    @NotNull
    private Periodicity periodicity;

}
