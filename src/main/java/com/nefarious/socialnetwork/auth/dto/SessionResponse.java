package com.nefarious.socialnetwork.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionResponse {
    private String accessToken;
    private String refreshToken;
}
