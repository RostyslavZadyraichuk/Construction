package org.zadyraichuk.construction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zadyraichuk.construction.entity.User;
import org.zadyraichuk.construction.repository.UserRepository;
import org.zadyraichuk.construction.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthenticationProvider authenticationProvider;
    private final UserRepository userRepository;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
                          CustomAuthenticationProvider authenticationProvider,
                          UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.userRepository = userRepository;
    }

    @Bean
    @Lazy
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/project/{id}/*", "/projects",
                        "/project/{id}").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/authorize")
                .usernameParameter("emailOrUsername")
                .passwordParameter("loginPassword")
                .loginProcessingUrl("/login")
                .permitAll()
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    User user = userRepository.findByEmail(authentication.getName()).orElse(
                            userRepository.findByUsername(authentication.getName())
                                    .orElse(null));
                    if (user != null) {
                        httpServletResponse.sendRedirect("/home");
                    }
                })
                .failureUrl("/authorize")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/access_denied");
    }

}
