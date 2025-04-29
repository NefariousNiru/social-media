package com.nefarious.socialnetwork.auth.service;

import com.nefarious.socialnetwork.annotation.RateLimit;
import com.nefarious.socialnetwork.auth.dto.*;
import com.nefarious.socialnetwork.auth.security.JwtTokenProvider;
import com.nefarious.socialnetwork.user.entity.User;
import com.nefarious.socialnetwork.exceptions.BusinessException;
import com.nefarious.socialnetwork.auth.service.interfaces.EmailService;
import com.nefarious.socialnetwork.user.service.UserService;
import com.nefarious.socialnetwork.auth.util.enums.AuthErrorCode;
import com.nefarious.socialnetwork.auth.util.enums.TokenType;
import jakarta.validation.Valid;
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
    public void signup(@Valid SignupRequest request) {
        userService.createUser(request);
        try {
            this.generateAndSaveAndEmailOtp(request.getEmail());
        } catch (BusinessException e) {
            log.warn("OTP email failed, but signup succeeded for {}: {}", request.getEmail(), e.getMessage());
        }
    }

    /**
     * Handles user signin requests. Adds a RateLimit AOP to limit queries to this window
     *
     * <p>Authenticates the user credentials, and upon successful authentication,
     * generates access and refresh tokens and creates a session.
     *
     * @param request {@link SigninRequest} containing email and password.
     * @return {@link SessionResponse} containing access and refresh tokens upon successful signin.
     */
    @RateLimit(key = "signin:{email}", property = "rate-limit.login-attempts")
    public SessionResponse signin(@Valid SigninRequest request) {
        userService.authenticate(request);
        User user = userService.getByEmail(request.getEmail());
        return this.generateTokenAndCreateSession(user.getId());
    }

    /**
     * Verify the OTP, mark the user as verified, and create session
     *
     * @param email Email of user
     * @param code  OTP sent by user
     * @return sessionResponse {@link SessionResponse} A session response object
     */
    public SessionResponse verifyOtpAndCreateSession(String email, String code) {
        if (!otpService.validateOtp(email, code)) throw new BusinessException(AuthErrorCode.INVALID_OTP);
        userService.markEmailVerified(email);
        User user = userService.getByEmail(email);
        return this.generateTokenAndCreateSession(user.getId());
    }

    /**
     * Generates a new OTP for the given email, saves it, and sends it via email. Rate-limited using RateLimit AOP.
     *
     * <p>Creates a one-time password (OTP), stores it for verification,
     * and emails the OTP to the provided address.
     *
     * @param email the email address to which the OTP will be sent.
     */
    @RateLimit(key = "otp:{email}", property = "rate-limit.otp-attempts")
    public void generateAndSaveAndEmailOtp(String email) {
        String code = otpService.generateAndSaveOtp(email);
        emailService.sendOtpEmail(email, code);
    }

    /**
     * Refresh and provide a new access and refresh token pair, using a valid existing refresh token.
     * @param refreshToken the refresh token
     */
    @RateLimit(key = "refresh:{refreshToken}", property = "rate-limit.refresh-attempts")
    public SessionResponse refreshSession(String refreshToken) {
        UUID userId = sessionService.validateRefreshToken(refreshToken).orElseThrow(() -> new BusinessException(AuthErrorCode.INVALID_CREDENTIALS));
        sessionService.revokeSession(refreshToken, TokenType.REFRESH);
        return this.generateTokenAndCreateSession(userId);
    }

    /**
     * Revoke access and refresh token
     * @param accessToken Access Token to be revoked
     * @param refreshToken RefreshToken to be revoked
     */
    public void revokeSession(String accessToken, String refreshToken) {
        sessionService.revokeSession(accessToken, TokenType.ACCESS);
        sessionService.revokeSession(refreshToken, TokenType.REFRESH);
    }

    /**
     * Initiates the forgot password process by generating and sending an OTP to the user's email.
     * Applies rate limiting based on the user's email to prevent abuse.
     *
     * @param request the forgot password request containing the user's email
     * @throws BusinessException if the user's email is not verified
     */
    @RateLimit(key = "forgot-password:{email}", property = "rate-limit.forgot-password-attempts")
    public void requestForgotPassword(ForgotPasswordRequest request) {
        User user = userService.getByEmail(request.getEmail());
        if (!user.isEmailVerified()) {
            throw new BusinessException(AuthErrorCode.EMAIL_NOT_VERIFIED);
        }
        this.generateAndSaveAndEmailOtp(request.getEmail());
    }

    /**
     * Verifies the OTP submitted during the forgot password process, updates the user's password,
     * and invalidates all active sessions for security.
     * Applies rate limiting based on the user's email to prevent brute-force attempts.
     *
     * @param request the forgot password verification request containing the email, OTP, and new password
     * @throws BusinessException if the OTP is invalid
     */
    @RateLimit(key = "verify-forgot-password:{email}", property = "rate-limit.forgot-password-attempts")
    public void verifyForgotPassword(ForgotPasswordVerifyRequest request) {
        if (!otpService.validateOtp(request.getEmail(), request.getCode())) {
            throw new BusinessException(AuthErrorCode.INVALID_OTP);
        }
        userService.updatePassword(request.getEmail(), request.getNewPassword());
        sessionService.invalidateAllSessionsForUser(userService.getByEmail(request.getEmail()).getId());
    }

    /**
     * Generate a token using {@link JwtTokenProvider} and create a session using {@link SessionService} (persist in Redis)
     * @param userId userId of user to create token against
     * @return sessionResponse {@link SessionResponse} type object
     */
    private SessionResponse generateTokenAndCreateSession(UUID userId) {
        String accessToken = jwtTokenProvider.generateToken(userId, TokenType.ACCESS);
        String refreshToken = jwtTokenProvider.generateToken(userId, TokenType.REFRESH);
        sessionService.createSession(accessToken, userId, TokenType.ACCESS);
        sessionService.createSession(refreshToken, userId, TokenType.REFRESH);
        return new SessionResponse(accessToken, refreshToken);
    }
}
