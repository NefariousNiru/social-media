package com.nefarious.socialnetwork.auth.service;

import com.nefarious.socialnetwork.auth.security.JwtTokenProvider;
import com.nefarious.socialnetwork.auth.util.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final StringRedisTemplate redis;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.access-expiration-ms}")
    private long accessExpirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    /** Creates a signed JWT access Token, store it in Redis under "tokenType:{token}" → "{userId}", set the same TTL as the JWT expiry
     * @param userId UserId of user
     * @param token the token to store
     * @param tokenType the type of token {@link TokenType}
     */
    public void createSession(String token, UUID userId, TokenType tokenType) {
        String key = tokenType.getValue() + ":" + token;
        long ttl = tokenType == TokenType.ACCESS ? accessExpirationMs : refreshExpirationMs;
        redis.opsForValue()
                .set(key, userId.toString(), Duration.ofMillis(ttl));
    }

    /** Validate Access Token */
    public Optional<UUID> validateAccessToken(String token) {
        return validateSession(token, TokenType.ACCESS);
    }

    /** Validate Refresh Token */
    public Optional<UUID> validateRefreshToken(String token) {
        return validateSession(token, TokenType.REFRESH);
    }

    /**
     * This method validates a session by checking if a token is valid.
     * @param token A jwt token
     * @param type Type of Token {@link TokenType}
     * @return Optional UUID of user
     */
    private Optional<UUID> validateSession(String token, TokenType type) {
        if (!jwtTokenProvider.validateToken(token)) return Optional.empty();
        if (type == TokenType.REFRESH && !jwtTokenProvider.isRefreshToken(token)) return Optional.empty();

        String key = type.getValue() + ":" + token;
        String redisUid = redis.opsForValue().get(key);
        if (redisUid == null) return Optional.empty();

        UUID tokenUid = jwtTokenProvider.getUserIdFromJwt(token);
        if (!tokenUid.toString().equals(redisUid)) return Optional.empty();

        return Optional.of(tokenUid);
    }

    /** Invalidate by deleting the key. */
    public void revokeSession(String token, TokenType tokenType) {
        String key = tokenType.getValue() + ":" + token;
        redis.delete(key);
    }
}
