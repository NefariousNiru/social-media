package com.nefarious.socialnetwork.user.service;

import com.nefarious.socialnetwork.auth.dto.SigninRequest;
import com.nefarious.socialnetwork.auth.dto.SignupRequest;
import com.nefarious.socialnetwork.user.entity.User;
import com.nefarious.socialnetwork.exceptions.BusinessException;
import com.nefarious.socialnetwork.user.repository.UserRepository;
import com.nefarious.socialnetwork.auth.util.enums.AuthErrorCode;
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
            throw new BusinessException(AuthErrorCode.EMAIL_IN_USE);
        }
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new BusinessException(AuthErrorCode.USERNAME_IN_USE);
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

    /**
     * Authenticates a user by verifying email and password credentials.
     *
     * <p>Throws {@link BusinessException} if credentials are invalid or email is not verified.
     *
     * @param signinRequest {@link SigninRequest} containing the user's email and password.
     */
    public void authenticate(SigninRequest signinRequest) {
        User user = this.getByEmail(signinRequest.getEmail());
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
        if (!user.isEmailVerified()) throw new BusinessException(AuthErrorCode.EMAIL_NOT_VERIFIED);
    }

    /**
     * Marks the user's email as verified.
     *
     * <p>Fetches the user by email, updates the verification status, and persists the change.
     *
     * @param email the email address of the user to mark as verified.
     */
    public void markEmailVerified(String email) {
        User user = this.getByEmail(email);
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    /**
     * Retrieves a user by email.
     *
     * <p>Throws {@link BusinessException} if no user is found with the given email.
     *
     * @param email the email address to search for.
     * @return {@link User} entity matching the provided email.
     */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(AuthErrorCode.USER_NOT_EXISTS));
    }

    /**
     * Updates a user's password
     * @param email Email of user
     * @param rawPassword Plaintext password of user
     */
    public void updatePassword(String email, String rawPassword) {
        User user = this.getByEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }
}
