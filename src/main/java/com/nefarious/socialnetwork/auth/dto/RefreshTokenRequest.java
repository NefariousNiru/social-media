package com.nefarious.socialnetwork.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** This class represents the request to refresh a token. Fields: refreshToken*/
@Data
public class RefreshTokenRequest {
    @NotBlank  private String refreshToken;
}
