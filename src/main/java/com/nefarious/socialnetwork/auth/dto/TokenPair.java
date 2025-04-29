package com.nefarious.socialnetwork.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** This class represents token pairs. Access and Refresh*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenPair {
    private String accessToken;
    private String refreshToken;
}
