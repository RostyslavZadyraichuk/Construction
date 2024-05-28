package org.zadyraichuk.construction.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.zadyraichuk.construction.dto.SubscriptionDTO;
import org.zadyraichuk.construction.entity.Subscription;
import org.zadyraichuk.construction.enumeration.Periodicity;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public abstract class SubscriptionMapper {

    @Mapping(source = "id", target = "operationId")
    @Mapping(target = "paymentDate",
            expression = "java(getPaymentDate(dto.getExpirationDate(), dto.getPeriodicity()))")
    @Mapping(source = "periodicity", target = "periodicity")
    @Mapping(source = "expirationDate", target = "expirationDate")
    public abstract Subscription toEntity(SubscriptionDTO dto);

    @Mapping(source = "operationId", target = "id")
    @Mapping(source = "periodicity", target = "periodicity")
    @Mapping(source = "expirationDate", target = "expirationDate")
    public abstract SubscriptionDTO toDTO(Subscription entity);

    public LocalDate getPaymentDate(LocalDate expirationDate, Periodicity periodicity) {
        switch (periodicity) {
            case MONTHLY:
                return expirationDate.minusMonths(1);
            case ANNUAL:
                return expirationDate.minusYears(1);
        }
        return null;
    }
}
