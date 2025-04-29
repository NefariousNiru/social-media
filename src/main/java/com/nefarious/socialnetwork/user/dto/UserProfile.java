package com.nefarious.socialnetwork.user.dto;

import com.nefarious.socialnetwork.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserProfile {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String profileImageUrl;
    private String organization;
    private String school;
    private String position;

    public static UserProfile from(User user) {
        return UserProfile.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .profileImageUrl(user.getProfileImageUrl())
                .organization(user.getOrganization())
                .school(user.getSchool())
                .position(user.getPosition())
                .build();
    }
}
