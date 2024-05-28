package org.zadyraichuk.construction.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zadyraichuk.construction.entity.User;
import org.zadyraichuk.construction.enumeration.Role;
import org.zadyraichuk.construction.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static List<GrantedAuthority> getAuthorities(Role[] roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(usernameOrEmail)
                .orElse(userRepository.findByEmail(usernameOrEmail)
                        .orElse(null));
        if (user == null) {
            throw new UsernameNotFoundException("No user found with " +
                    "username/email:" + usernameOrEmail);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword().toLowerCase(), enabled,
                accountNonExpired, credentialsNonExpired, accountNonLocked,
                getAuthorities(user.getRoles()));
    }
}
