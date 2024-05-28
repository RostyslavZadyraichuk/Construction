package org.zadyraichuk.construction.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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
    private ObjectId id;

    @Field
    private Role[] roles;

    @Field
    @NotNull
    private String email;

    @Field(name = "full_name")
    private String fullName;

    @Field(name = "username")
    @NotNull
    private String username;

    @Field(name = "password")
    @NotNull
    private String password;

    @Field(name = "new_messages")
    private String[] newMessages;

    @Field(name = "checked_messages")
    private String[] checkedMessages;

    @Field(name = "has_picture")
    private Boolean hasPicture = false;

//    @Field(name = "is_verified")
//    @NotNull
//    private Boolean isVerified = false;

    @Field
    private Subscription subscription;

//    @Field(name = "is_new_company_owner")
//    @NotNull
//    private Boolean isNewCompanyOwner = true;

    public boolean hasPicture() {
        return hasPicture;
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
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
