package com.nefarious.socialnetwork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) disable CSRF for now (you can enable it later if you use cookies)
                .csrf(csrf -> csrf.disable())

                // 2) configure URL authorization
                .authorizeHttpRequests(auth -> auth
                        // allow unauthenticated access to any /auth/** endpoint
                        .requestMatchers("/auth/**").permitAll()
                        // everything else requires a valid token
                        .anyRequest().authenticated()
                )

                // 3) we’re stateless (no HTTP session)
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 4) disable default form login & basic auth
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.disable())
        ;

        // 5) TODO: insert your token-validation filter here:
        // http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
