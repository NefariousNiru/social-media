package com.nefarious.socialnetwork.follow.service;

import com.nefarious.socialnetwork.follow.repository.FollowRelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
    private final FollowRelationshipRepository followRelationshipRepository;

    public void follow(UUID fromUserId, String toUsername) {
        System.out.println("WOW: " + fromUserId + " " + toUsername);
        followRelationshipRepository.follow(fromUserId.toString(), toUsername);
    }

    public void unfollow(UUID fromUserId, String toUsername) {
        followRelationshipRepository.unfollow(fromUserId.toString(), toUsername);
    }

    public List<String> getFollowers(String username) {
        return followRelationshipRepository.getFollowers(username);
    }

    public List<String> getFollowing(String username) {
        return followRelationshipRepository.getFollowing(username);
    }
}
