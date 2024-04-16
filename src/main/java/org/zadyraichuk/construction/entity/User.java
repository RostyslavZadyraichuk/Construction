package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Document(collection = "user")
public class User {

    @Id
    @Indexed(unique = true)
    private final ObjectId id = null;

    @Field
    @NotNull
    private Role role = Role.GENERAL_CONTRACTOR;

    @Field(name = "full_name")
    private final String fullName = null;

    @Field(name = "username")
    @NotNull
    @Indexed(unique = true)
    private String userName;

    @Field(name = "password")
    @NotNull
    private String encodedPassword;

    @Field(name = "new_messages")
    private String[] newMessages;

    @Field(name = "checked_messages")
    private String[] checkedMessages;

    @Field(name = "is_verified")
    @NotNull
    private Boolean isVerified = false;

    @Field
    private Subscription subscription;

    @Field(name = "is_new_company_owner")
    @NotNull
    private Boolean isNewCompanyOwner = true;

}
