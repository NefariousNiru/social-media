package com.nefarious.socialnetwork.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


/**
 * Redis Hash Otp Entry with <key, value> -> <(otp:email): (otp, ttl)>
 * Time to Live for OTP is 600s (10 minutes)
 */
@RedisHash(value = "otp", timeToLive = 600)
@Data @AllArgsConstructor @Builder @NoArgsConstructor
public class OtpEntry {

    @Id
    private String email;
    private String code;
}
