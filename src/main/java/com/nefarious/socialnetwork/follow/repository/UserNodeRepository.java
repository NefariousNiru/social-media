package com.nefarious.socialnetwork.follow.repository;

import com.nefarious.socialnetwork.follow.dto.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import java.util.UUID;

public interface UserNodeRepository extends Neo4jRepository<UserNode, UUID> {
}
