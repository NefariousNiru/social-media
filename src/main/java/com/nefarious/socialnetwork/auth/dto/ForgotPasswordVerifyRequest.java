package com.nefarious.socialnetwork.auth.dto;

import com.nefarious.socialnetwork.auth.util.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Request body class for a forgot password verify request. Fields: Email*/
@EqualsAndHashCode(callSuper = true)
@Data
public class ForgotPasswordVerifyRequest extends OtpVerificationRequest {
    @NotBlank
    @Size(min = 8, max = 24)
    @Pattern(regexp = Constants.VALID_PASSWORD_REGEXP,
            message = Constants.INVALID_PASSWORD_COMBINATION)
    private String newPassword;
}
