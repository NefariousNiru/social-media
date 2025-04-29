package com.nefarious.socialnetwork.annotation;

import java.lang.annotation.*;

/**
 * Custom annotation to apply rate limiting to controller/service methods.
 *
 * <p>Usage example:
 * <pre>
 *     @RateLimit(key = "signin:{email}", property = "rate-limit.login-attempts")
 *     public void login(String email) { ... }
 * </pre>
 *
 * @param key Redis key template. Use placeholders like {email} to be replaced by method arguments.
 * @param property Application property name that specifies the rate limit threshold.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    String key();          // Redis key template like "otp:{email}"
    String property();     // Property name like "rate-limit.login-attempts"
}
