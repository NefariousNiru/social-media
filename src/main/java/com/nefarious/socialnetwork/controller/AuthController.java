package com.nefarious.socialnetwork.controller;

import com.nefarious.socialnetwork.config.JwtTokenProvider;
import com.nefarious.socialnetwork.dto.OtpVerificationRequest;
import com.nefarious.socialnetwork.dto.SessionResponse;
import com.nefarious.socialnetwork.entity.User;
import com.nefarious.socialnetwork.service.AuthService;
import com.nefarious.socialnetwork.service.SessionService;
import com.nefarious.socialnetwork.service.UserService;
import com.nefarious.socialnetwork.util.Endpoints;
import com.nefarious.socialnetwork.dto.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(Endpoints.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final SessionService sessionService;
    private final UserService userService;

    /**
     * Handles user signup requests.
     *
     * <p>Accepts a JSON payload containing user registration information,
     * validates it, and delegates user creation to the {@link AuthService}.
     *
     * @param request {@link SignupRequest} the signup request data containing email, password, username, etc.
     * @return 202 Accepted if signup request is successfully processed
     */
    @PostMapping(Endpoints.AUTH_SIGNUP)
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(Endpoints.AUTH_VERIFY)
    public ResponseEntity<SessionResponse> verify(@Valid @RequestBody OtpVerificationRequest request) {
        authService.verifyOtp(request.getEmail(), request.getCode());
        String token = authService.generateTokenAndCreateSession(request);
        return ResponseEntity.ok(new SessionResponse(token));
    }
}
