package com.nefarious.socialnetwork.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/** Request body class for a forgot password request. Fields: Email*/
@Data
public class ForgotPasswordRequest {
    @Email @NotBlank private String email;
}
