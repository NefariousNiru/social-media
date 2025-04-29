package com.nefarious.socialnetwork.auth.dto;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** This class is used to capture the data required to send a Session Response. Fields refreshToken, accessToken.
 *  Used as a Context Alias for TokenPair
 */
@NoArgsConstructor
public class SessionResponse extends TokenPair {
    public SessionResponse(String accessToken, String refreshToken) {
        super(accessToken, refreshToken);
    }
}
