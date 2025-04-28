package com.nefarious.socialnetwork.service;

import com.nefarious.socialnetwork.dto.SignupRequest;
import com.nefarious.socialnetwork.entity.User;
import com.nefarious.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;    // Auto Injected from a bean in SecurityConfig.java

    /**
     * Creates a new user after validating the provided email and username.
     *
     * @param signupRequest {@link SignupRequest} the request containing user details for signup
     * @throws IllegalArgumentException if the email or username is already in use
     */
    public void createUser(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already in use");
        }

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .username(signupRequest.getUsername())
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .dateOfBirth(signupRequest.getDateOfBirth())
                .isEmailVerified(false)
                .build();

        userRepository.save(user);
    }

    public void markEmailVerified(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No such user"));
        user.setEmailVerified(true);
        userRepository.save(user);          // persists the change
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No such user"));
    }
}
