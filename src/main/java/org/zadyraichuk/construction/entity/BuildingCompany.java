package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Document(collection = "company")
public class BuildingCompany {

    @Id
    @Indexed(unique = true)
    private ObjectId id;

    @Field
    @NotNull
    private String name;

    @Field
    private String description;

    @Field
    @NotNull
    private String email;

    @Field
    @NotNull
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @Field(name = "office_location")
    @NotNull
    private String officeLocation;

    @Field
    private String website;

    @Field(name = "company_owner_id")
    @NotNull
    private ObjectId companyOwnerUserId;

    @Field(name = "company_owner_full_name")
    @NotNull
    private String companyOwnerFullName;

    @Field(name = "has_picture")
    private Boolean hasPicture = false;

    public boolean hasPicture() {
        return hasPicture;
    }

}
