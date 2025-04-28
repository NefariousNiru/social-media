package com.nefarious.socialnetwork.auth.security;

import com.nefarious.socialnetwork.auth.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Custom authentication filter that processes Bearer tokens in the Authorization header.
 * Validates the token using the {@link SessionService} and sets the authentication context if valid.
 */
@RequiredArgsConstructor
public class AuthTokenBearer extends OncePerRequestFilter {
    private final SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            sessionService.validateAccessToken(token)
                    .ifPresent(userId -> {
                        //  Build an Authentication with the userId as principal
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                userId,    // principal
                                null,      // credentials
                                List.of()  // authorities (you can load roles later)
                        );
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    });
        }
        filterChain.doFilter(request, response);
    }
}
