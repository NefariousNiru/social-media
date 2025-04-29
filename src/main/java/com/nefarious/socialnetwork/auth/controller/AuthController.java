package com.nefarious.socialnetwork.auth.controller;

import com.nefarious.socialnetwork.auth.dto.*;
import com.nefarious.socialnetwork.auth.service.AuthService;
import com.nefarious.socialnetwork.auth.util.AuthEndpoint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(AuthEndpoint.AUTH_V1)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Handles user signup requests.
     *
     * <p>Accepts a JSON payload containing user registration information,
     * validates it, and delegates user creation to the {@link AuthService}.
     *
     * @param request {@link SignupRequest} the signup request data containing email, password, username, etc.
     * @return 202 Accepted if signup request is successfully processed
     */
    @PostMapping(AuthEndpoint.AUTH_SIGNUP)
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.accepted().build();
    }

    /**
     * Handles user signin requests.
     *
     * <p>Accepts a JSON payload containing user credentials,
     * authenticates the user, and returns a session with access and refresh tokens.
     *
     * @param request {@link SigninRequest} containing email and password.
     * @return 200 OK with {@link SessionResponse} containing tokens if authentication succeeds.
     */
    @PostMapping(AuthEndpoint.AUTH_SIGNIN)
    public ResponseEntity<SessionResponse> signin(@Valid @RequestBody SigninRequest request) {
        SessionResponse response = authService.signin(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles OTP verification requests during signup.
     *
     * <p>Accepts a JSON payload with email and OTP code,
     * verifies the OTP, marks the user's email as verified,
     * and generates session tokens.
     *
     * @param request {@link OtpVerificationRequest} containing email and OTP code.
     * @return 200 OK with {@link SessionResponse} containing access and refresh tokens upon successful verification.
     */
    @PostMapping(AuthEndpoint.AUTH_VERIFY)
    public ResponseEntity<SessionResponse> verify(@Valid @RequestBody OtpVerificationRequest request) {
        SessionResponse response = authService.verifyOtpAndCreateSession(request.getEmail(), request.getCode());
        return ResponseEntity.ok(response);
    }

    /**
     * Handles requests to resend OTP during signup.
     *
     * <p>Accepts an email as a request parameter,
     * generates a new OTP, saves it, and sends it to the user's email address.
     *
     * @param email the email address to which the OTP will be sent. Must be a valid email.
     * @return 202 Accepted if the OTP is successfully generated and sent.
     */
    @GetMapping(AuthEndpoint.RESEND_OTP)
    public ResponseEntity<Void> resendOtp(@RequestParam @Email String email) {
        authService.generateAndSaveAndEmailOtp(email);
        return ResponseEntity.accepted().build();
    }

    /**
     * Refreshes the session by generating new access and refresh tokens.
     *
     * @param request the refresh token request containing the old refresh token
     * @return a new session response with updated tokens
     */
    @PostMapping(AuthEndpoint.REFRESH)
    public ResponseEntity<SessionResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        SessionResponse response = authService.refreshSession(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    /**
     * Logs out the user by revoking their access and refresh tokens.
     *
     * @param request the logout request containing access and refresh tokens
     * @return an empty response indicating successful logout
     */
    @PostMapping(AuthEndpoint.LOGOUT)
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authService.revokeSession(request.getAccessToken(), request.getRefreshToken());
        return ResponseEntity.ok().build();
    }

    /**
     * Initiates the forgot password process by sending a reset link or OTP to the user.
     *
     * @param request the forgot password request containing user identification (e.g., email)
     * @return an accepted response indicating the request was processed
     */
    @PostMapping(AuthEndpoint.FORGOT_PASSWORD)
    public ResponseEntity<Void> forgotPasswordRequest(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.requestForgotPassword(request);
        return ResponseEntity.accepted().build();
    }

    /**
     * Verifies the forgot password request by validating the OTP or reset token and updating the password.
     *
     * @param request the forgot password verification request containing verification details
     * @return an OK response indicating successful password reset
     */
    @PostMapping(AuthEndpoint.FORGOT_PASSWORD_VERIFY)
    public ResponseEntity<Void> forgotPasswordVerify(@Valid @RequestBody ForgotPasswordVerifyRequest request) {
        authService.verifyForgotPassword(request);
        return ResponseEntity.ok().build();
    }
}
