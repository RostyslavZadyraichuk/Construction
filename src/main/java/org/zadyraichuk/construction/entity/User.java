package org.zadyraichuk.construction.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
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


    public User() {
        this.id = null;
        this.fullName = null;
    }

    public User(String fullName) {
        this.id = null;
        this.fullName = fullName;
    }

    @PersistenceConstructor
    public User(ObjectId id,
                Role role,
                String email,
                String fullName,
                String encodedPassword,
                String[] newMessages,
                String[] checkedMessages,
                Subscription subscription,
                Boolean isNewCompanyOwner) {
        this.id = id;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
        this.encodedPassword = encodedPassword;
        this.newMessages = newMessages;
        this.checkedMessages = checkedMessages;
        this.subscription = subscription;
        this.isNewCompanyOwner = isNewCompanyOwner;
    }

}
