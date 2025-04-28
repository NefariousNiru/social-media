package com.nefarious.socialnetwork.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** This class is used to capture email and otp. It also performs certain validations.*/
@Data
public class OtpVerificationRequest {
    @Email @NotBlank private String email;
    @NotBlank @Size(min=10, max=10) @Pattern(regexp = "^[A-Za-z0-9]{10}$", message = "Invalid OTP") private String code;
}
