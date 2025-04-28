package com.nefarious.socialnetwork.service;

import com.nefarious.socialnetwork.config.JwtTokenProvider;
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

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    /** Creates a signed JWT, store it in Redis under "session:{token}" → "{userId}", set the same TTL as the JWT expiry
     * @param userId UserId of user
     * @param token the token to store
     */
    public void createSession(String token, UUID userId) {
        redis.opsForValue()
                .set("session:" + token, userId.toString(), Duration.ofMillis(jwtExpirationMs));
    }

    /**
     * This method validates a session.
     * First it validates if a token is valid
     * Then it validates if the token is valid inside redis
     * Then it validates if the owner of the token is same as the setter
     * @param token A jwt token
     * @return Optional UUID of user
     */
    public Optional<UUID> validateSession(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            return Optional.empty();
        }
        String redisUid = redis.opsForValue().get("session:" + token);
        if (redisUid == null) {
            return Optional.empty();
        }

        UUID tokenUid = jwtTokenProvider.getUserIdFromJwt(token);
        if (!tokenUid.toString().equals(redisUid)) {
            return Optional.empty();
        }

        return Optional.of(tokenUid);
    }

    /** Invalidate by deleting the key. */
    public void revokeSession(String token) {
        redis.delete("session:" + token);
    }


}
