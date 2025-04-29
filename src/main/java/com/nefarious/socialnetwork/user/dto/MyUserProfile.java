package com.nefarious.socialnetwork.user.dto;


import com.nefarious.socialnetwork.user.aop.MinimumAge;
import com.nefarious.socialnetwork.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/** Extends the UserProfile by adding sensitive info when the user requests their own profile */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyUserProfile extends UserProfile {
    @Past @MinimumAge private LocalDate dateOfBirth;
    @Email private String email;

    public MyUserProfile(User user) {
        super(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getProfileImageUrl(),
                user.getSchool(),
                user.getOrganization(),
                user.getPosition()
        );
        // Add sensitive fields here
        this.dateOfBirth = user.getDateOfBirth();
        this.email = user.getEmail();
    }

    public static MyUserProfile from(User user) {
        return new MyUserProfile(user);
    }
}
