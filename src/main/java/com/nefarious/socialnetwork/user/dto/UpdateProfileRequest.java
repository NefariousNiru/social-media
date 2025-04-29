package com.nefarious.socialnetwork.user.dto;

import com.nefarious.socialnetwork.util.Constants;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UpdateProfileRequest {
    @Size(max = 30, message = Constants.INVALID_SIZE_NAME) private String firstName;
    @Size(max = 30, message = Constants.INVALID_SIZE_NAME) private String lastName;
    @Size(max = 250, message = Constants.INVALID_BIO_SIZE) private String bio;
    @URL(message = Constants.INVALID_URL) private String profileImageUrl;
}
