package com.nefarious.socialnetwork.follow.controller;

import com.nefarious.socialnetwork.follow.service.FollowService;
import com.nefarious.socialnetwork.follow.util.FollowEndpoint;
import com.nefarious.socialnetwork.user.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(FollowEndpoint.FOLLOW_V1)
public class FollowController {
    private final FollowService followService;

    @PostMapping(FollowEndpoint.BY_USERNAME)
    public ResponseEntity<Void> follow(@PathVariable String username) {
        UUID currUserId = CurrentUser.getPrincipal();
        followService.follow(currUserId, username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(FollowEndpoint.BY_USERNAME)
    public ResponseEntity<Void> unfollow(@PathVariable String username) {
        UUID currUserId = CurrentUser.getPrincipal();
        followService.unfollow(currUserId, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping(FollowEndpoint.FOLLOWERS_BY_USERNAME)
    public ResponseEntity<List<String>> getFollowers(@PathVariable String username) {
        return ResponseEntity.ok(followService.getFollowers(username));
    }

    @GetMapping(FollowEndpoint.FOLLOWING_BY_USERNAME)
    public ResponseEntity<List<String>> getFollowing(@PathVariable String username) {
        return ResponseEntity.ok(followService.getFollowing(username));
    }
}
