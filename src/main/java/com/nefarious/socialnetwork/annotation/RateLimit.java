package com.nefarious.socialnetwork.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    String key();          // Redis key template like "otp:{email}"
    String property();     // Property name like "rate-limit.login-attempts"
}
