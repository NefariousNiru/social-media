package com.nefarious.socialnetwork.auth.dto;

import lombok.NoArgsConstructor;

/** Used as a Context Alias for TokenPair to process Logout Requests*/
@NoArgsConstructor
public class LogoutRequest extends TokenPair {
    public LogoutRequest(String accessToken, String refreshToken) {
        super(accessToken, refreshToken);
    }
}
