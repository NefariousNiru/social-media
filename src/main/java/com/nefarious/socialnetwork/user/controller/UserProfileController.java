package com.nefarious.socialnetwork.user.controller;

import com.nefarious.socialnetwork.user.dto.MyUserProfile;
import com.nefarious.socialnetwork.user.dto.UpdateProfileRequest;
import com.nefarious.socialnetwork.user.dto.UserProfile;
import com.nefarious.socialnetwork.user.service.UserProfileService;
import com.nefarious.socialnetwork.user.util.UserEndpoint;
import com.nefarious.socialnetwork.user.util.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping(UserEndpoint.USER_V1)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    /**
     * Retrieves the public profile of a user by their username.
     *
     * @param username the username of the user whose profile is to be retrieved
     * @return ResponseEntity containing the user's profile data
     */
    @GetMapping(UserEndpoint.PUBLIC_BY_USERNAME)
    public ResponseEntity<UserProfile> getProfile(@PathVariable String username) {
        UserProfile profile = userProfileService.getProfileByUsername(username);
        return ResponseEntity.ok(profile);
    }

    /**
     * Retrieves the private profile of a user by their userid only user themselves can call it (Principal).
     * @return ResponseEntity with HTTP 200 if update is successful
     */
    @GetMapping(UserEndpoint.ME)
    public ResponseEntity<MyUserProfile> getProfile() {
        UUID userId = CurrentUser.getPrincipal();
        MyUserProfile myProfile = userProfileService.getMyProfileById(userId);
        return ResponseEntity.ok(myProfile);
    }

    /**
     * Updates the profile of the currently authenticated user.
     * Only non-null fields in the request will be updated.
     *
     * @param request the profile data to be updated
     * @return ResponseEntity with HTTP 200 if update is successful
     */
    @PatchMapping(UserEndpoint.ME)
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UUID userId = CurrentUser.getPrincipal();
        userProfileService.updateProfile(userId, request);
        return ResponseEntity.ok().build();
    }
}
