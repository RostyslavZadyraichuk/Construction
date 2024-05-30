package org.zadyraichuk.construction.service.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Named;

public abstract class GeneralMapper {

    @Named("toObjectId")
    public ObjectId toObjectId(String userId) {
        return new ObjectId(userId);
    }

    @Named("toStringId")
    public String toStringId(ObjectId id) {
        return id.toHexString();
    }

}
