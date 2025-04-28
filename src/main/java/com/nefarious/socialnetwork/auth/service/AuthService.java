package com.nefarious.socialnetwork.auth.service;

import com.nefarious.socialnetwork.auth.security.JwtTokenProvider;
import com.nefarious.socialnetwork.auth.dto.SigninRequest;
import com.nefarious.socialnetwork.auth.dto.SignupRequest;
import com.nefarious.socialnetwork.auth.dto.SessionResponse;
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
     * Handles user signin requests.
     *
     * <p>Authenticates the user credentials, and upon successful authentication,
     * generates access and refresh tokens and creates a session.
     *
     * @param request {@link SigninRequest} containing email and password.
     * @return {@link SessionResponse} containing access and refresh tokens upon successful signin.
     */
    public SessionResponse signin(@Valid SigninRequest request) {
        userService.authenticate(request);
        return this.generateTokenAndCreateSession(request.getEmail());
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
        return this.generateTokenAndCreateSession(email);
    }

    /**
     * Generates a new OTP for the given email, saves it, and sends it via email.
     *
     * <p>Creates a one-time password (OTP), stores it for verification,
     * and emails the OTP to the provided address.
     *
     * @param email the email address to which the OTP will be sent.
     */
    public void generateAndSaveAndEmailOtp(String email) {
        String code = otpService.generateAndSaveOtp(email);
        emailService.sendOtpEmail(email, code);
    }

    /**
     * Generate a token using {@link JwtTokenProvider} and create a session using {@link SessionService} (persist in Redis)
     * @param email email of user to create token against
     * @return sessionResponse {@link SessionResponse} type object
     */
    private SessionResponse generateTokenAndCreateSession(String email) {
        User user = userService.getByEmail(email);
        String accessToken = jwtTokenProvider.generateToken(user.getId(), TokenType.ACCESS);
        String refreshToken = jwtTokenProvider.generateToken(user.getId(), TokenType.REFRESH);
        sessionService.createSession(accessToken, user.getId(), TokenType.ACCESS);
        sessionService.createSession(refreshToken, user.getId(), TokenType.REFRESH);
        return new SessionResponse(accessToken, refreshToken);
    }
}
