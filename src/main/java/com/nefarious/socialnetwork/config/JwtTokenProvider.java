package com.nefarious.socialnetwork.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        verifier = JWT.require(algorithm).build();
    }

    /** Generate a signed JWT with subject=userId and expiry */
    public String generateToken(UUID userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);

        return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(now)
                .withExpiresAt(exp)
                .sign(algorithm);
    }

    /** Validate the token (signature + exp). Returns true if valid */
    public boolean validateToken(String token) {
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    /** Extract the userId from token’s subject claim */
    public UUID getUserIdFromJwt(String token) {
        DecodedJWT decoded = verifier.verify(token);
        String subject = decoded.getSubject();
        return UUID.fromString(subject);
    }
}
