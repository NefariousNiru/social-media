package com.nefarious.socialnetwork.user.service;

import com.nefarious.socialnetwork.auth.util.enums.AuthErrorCode;
import com.nefarious.socialnetwork.exceptions.BusinessException;
import com.nefarious.socialnetwork.user.dto.UpdateProfileRequest;
import com.nefarious.socialnetwork.user.dto.UserProfile;
import com.nefarious.socialnetwork.user.entity.User;
import com.nefarious.socialnetwork.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserRepository userRepository;

    /**
     * Retrieves a user's public profile by their username.
     *
     * @param username the username of the user whose profile is to be retrieved
     * @return the UserProfile DTO representing the user's public profile
     * @throws BusinessException if the user does not exist
     */
    public UserProfile getProfileByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(AuthErrorCode.USER_NOT_EXISTS));
        return UserProfile.from(user);
    }

    /**
     * Updates the profile information of the specified user.
     * Only non-null fields in the request will be applied.
     *
     * @param userId the UUID of the user whose profile is to be updated
     * @param request the new profile values to update
     * @throws BusinessException if the user does not exist
     */
    @Transactional
    public void updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException(AuthErrorCode.USER_NOT_EXISTS));
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getBio() != null) user.setBio(request.getBio());
        if (request.getProfileImageUrl() != null) user.setProfileImageUrl(request.getProfileImageUrl());
        userRepository.save(user);
    }
}
