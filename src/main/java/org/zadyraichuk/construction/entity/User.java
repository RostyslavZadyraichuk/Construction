package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.zadyraichuk.construction.enumeration.Role;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Document(collection = "user")
public class User {

    @Id
    @Indexed(unique = true)
    private final ObjectId id;

    @Field
    @NotNull
    private Role role = Role.GENERAL_CONTRACTOR;

    @Field
    @NotNull
    private String email;

    @Field(name = "full_name")
    private final String fullName;

//    @Field(name = "username")
//    @NotNull
//    @Indexed(unique = true)
//    private String userName;

    @Field(name = "password")
    @NotNull
    private String encodedPassword;

    @Field(name = "new_messages")
    private String[] newMessages;

    @Field(name = "checked_messages")
    private String[] checkedMessages;

//    @Field(name = "is_verified")
//    @NotNull
//    private Boolean isVerified = false;

    @Field
    private Subscription subscription;

    @Field(name = "is_new_company_owner")
    @NotNull
    private Boolean isNewCompanyOwner = true;


    public User(ObjectId id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

}
