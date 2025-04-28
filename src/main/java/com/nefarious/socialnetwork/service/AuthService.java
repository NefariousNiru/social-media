package com.nefarious.socialnetwork.service;

import com.nefarious.socialnetwork.config.JwtTokenProvider;
import com.nefarious.socialnetwork.dto.OtpVerificationRequest;
import com.nefarious.socialnetwork.dto.SessionResponse;
import com.nefarious.socialnetwork.dto.SignupRequest;
import com.nefarious.socialnetwork.entity.User;
import com.nefarious.socialnetwork.exceptions.EmailSendException;
import com.nefarious.socialnetwork.service.interfaces.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SessionService sessionService;

    /**
     * Handles the user signup process.
     * <p>
     * This method creates a new user, generates an OTP, and sends it to the user's email.
     * If email sending fails, it logs the error without interrupting the signup process.
     *
     * @param request {@link SignupRequest} The signup request containing user information.
     */
    public void signup(SignupRequest request) {
        userService.createUser(request);
        String code = otpService.generateAndSaveOtp(request.getEmail());
        try {
            emailService.sendOtpEmail(request.getEmail(), code);
        } catch (EmailSendException e) {
            log.error("Failed to send OTP to {}: {}", request.getEmail(), e.getMessage());
        }
    }

    /**
     * Verify the OTP, mark the user as verified, and return true.
     *
     * @param email Email of user
     * @param code  OTP sent by user
     */
    public void verifyOtp(String email, String code) {
        if (!otpService.validateOtp(email, code)) throw new IllegalArgumentException("Invalid or Expired OTP");
        userService.markEmailVerified(email);
    }


    public String generateTokenAndCreateSession(OtpVerificationRequest request) {
        User user = userService.getByEmail(request.getEmail());
        String token = this.generateToken(user.getId());
        sessionService.createSession(token, user.getId());
        return token;
    }


    private String generateToken(UUID userId) {
        return jwtTokenProvider.generateToken(userId);
    }



}
