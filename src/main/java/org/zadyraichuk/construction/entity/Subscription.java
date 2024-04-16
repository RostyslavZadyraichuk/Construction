package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.zadyraichuk.construction.enumeration.UsageType;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class Subscription {

    @Field(name = "operation_id")
    @Indexed(unique = true)
    @NotNull
    private final String operationId;

    @Field(name = "usage_type")
    @NotNull
    private final UsageType usageType;

    @Field(name = "payment_date")
    @NotNull
    private final LocalDate paymentDate;

    @Field(name = "expiration_date")
    @NotNull
    private final LocalDate expirationDate;

    @Field(name = "is_paid")
    @NotNull
    private Boolean isPaid = false;

    public Subscription(String operationId,
                        UsageType usageType,
                        LocalDate paymentDate,
                        LocalDate expirationDate) {
        this.operationId = operationId;
        this.usageType = usageType;
        this.paymentDate = paymentDate;
        this.expirationDate = expirationDate;
    }
}
