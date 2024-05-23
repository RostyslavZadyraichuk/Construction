package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.zadyraichuk.construction.enumeration.Role;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Document(collection = "user")
public class User implements UserDetails {

    @Id
    @Indexed(unique = true)
    private final ObjectId id;

    @Field
    @NotNull
    private Role[] roles;

    @Field
    @NotNull
    private String email;

    @Field(name = "full_name")
    private final String fullName;

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
                Role[] roles,
                String email,
                String fullName,
                String userName,
                String encodedPassword,
                String[] newMessages,
                String[] checkedMessages,
                Subscription subscription,
                Boolean isNewCompanyOwner) {
        this.id = id;
        this.roles = roles;
        this.email = email;
        this.fullName = fullName;
        this.userName = userName;
        this.encodedPassword = encodedPassword;
        this.newMessages = newMessages;
        this.checkedMessages = checkedMessages;
        this.subscription = subscription;
        this.isNewCompanyOwner = isNewCompanyOwner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            authorities.add((GrantedAuthority) role::getRole);
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    @Override
    public String getUsername() {
        return userName;
    }
}
