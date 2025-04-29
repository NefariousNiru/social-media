package com.nefarious.socialnetwork.auth.config;

import com.nefarious.socialnetwork.auth.security.AuthTokenBearer;
import com.nefarious.socialnetwork.auth.service.SessionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfig {
    /**
     * Defines a {@link PasswordEncoder} bean using BCrypt hashing algorithm.
     * @return a new {@link BCryptPasswordEncoder} instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines a {@link AuthTokenBearer} bean used to validate and authenticate bearer tokens.
     * @param sessionService {@link SessionService} service to validate user sessions
     * @return a new {@link AuthTokenBearer} instance
     */
    @Bean
    public AuthTokenBearer authTokenBearer(SessionService sessionService) {
        return new AuthTokenBearer(sessionService);
    }

    /**
     * Defines the {@link SecurityFilterChain} bean.
     * Configures HTTP security including disabling CSRF, setting session management to stateless,
     * permitting unauthenticated access to `/auth/**` routes, and requiring authentication for all other requests.
     * Adds the {@link AuthTokenBearer} filter before the default {@link UsernamePasswordAuthenticationFilter}.
     *
     * @param http the {@link HttpSecurity} builder
     * @param authTokenBearer the custom authentication filter
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception in case of configuration errors
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenBearer authTokenBearer) throws Exception {
        http
            // 1) disable CSRF for now (enable it later if using cookies)
            .csrf(AbstractHttpConfigurer::disable)

            // 2) configure URL authorization
            .authorizeHttpRequests(auth -> auth
                    // allow unauthenticated access to any /auth/** endpoint
                    .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    // everything else requires a valid token
                    .anyRequest().authenticated()
            )

            // 3) we’re stateless (no HTTP session)
            .sessionManagement(sess ->
                    sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 4) Add AuthTokenBearer Filter
            .addFilterBefore(authTokenBearer, UsernamePasswordAuthenticationFilter.class)

            // 5) disable default form login & basic auth
            .httpBasic(Customizer.withDefaults())
            .formLogin(AbstractHttpConfigurer::disable)
        ;
        return http.build();
    }
}
