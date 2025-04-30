package com.nefarious.socialnetwork.follow.service;

import com.nefarious.socialnetwork.follow.dto.UserNode;
import com.nefarious.socialnetwork.follow.repository.UserNodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserNodeSyncService {
    private final UserNodeRepository userNodeRepository;

    public void persistUserNode(UUID userId, String username) {
        if (!userNodeRepository.existsById(userId)) userNodeRepository.save(new UserNode(userId, username, List.of()));
    }
}
