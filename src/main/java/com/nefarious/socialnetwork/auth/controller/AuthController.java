package com.nefarious.socialnetwork.auth.controller;

import com.nefarious.socialnetwork.auth.dto.OtpVerificationRequest;
import com.nefarious.socialnetwork.auth.dto.SessionResponse;
import com.nefarious.socialnetwork.auth.dto.SigninRequest;
import com.nefarious.socialnetwork.auth.service.AuthService;
import com.nefarious.socialnetwork.auth.util.AuthEndpoint;
import com.nefarious.socialnetwork.auth.dto.SignupRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(AuthEndpoint.AUTH)
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
}
