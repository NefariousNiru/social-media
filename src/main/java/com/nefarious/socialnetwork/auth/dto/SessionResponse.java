package com.nefarious.socialnetwork.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/** This class is used to capture the data required to send a Session Response. Fields refreshToken, accessToken*/
@Data
@AllArgsConstructor
public class SessionResponse {
    private String accessToken;
    private String refreshToken;
}
