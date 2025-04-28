package com.nefarious.socialnetwork.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

/** This class is used to capture the data required for user registration, including validation
 * for the email, password, username, firstname, lastname, and date of birth fields
 * */
@Data
public class SignupRequest {
    @Email @NotBlank private String email;
    @NotBlank @Size(min = 8, max = 24)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,24}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;
    @NotBlank @Size(min=1, max = 30) private String username;
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @NotNull @Past LocalDate dateOfBirth;
}
