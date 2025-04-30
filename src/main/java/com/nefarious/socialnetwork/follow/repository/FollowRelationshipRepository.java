package com.nefarious.socialnetwork.follow.repository;

import com.nefarious.socialnetwork.follow.dto.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import java.util.List;
import java.util.UUID;


public interface FollowRelationshipRepository extends Neo4jRepository<UserNode, UUID> {
    @Query("""
        MATCH (a:User {id: $fromUserId})
        MATCH (b:User {username: $toUsername})
        MERGE (a)-[:FOLLOWS]->(b)
    """)
    void follow(String fromUserId, String toUsername);

    @Query("""
        MATCH (a:User {id: $fromUserId})-[r:FOLLOWS]->(b:User {username: $toUsername})
        DELETE r
    """)
    void unfollow(String fromUserId, String toUsername);

    @Query("""
        MATCH (target:User {username: $username})<-[:FOLLOWS]-(follower:User)
        RETURN follower.username
    """)
    List<String> getFollowers(String username);

    @Query("""
        MATCH (user:User {username: $username})-[:FOLLOWS]->(following:User)
        RETURN following.username
    """)
    List<String> getFollowing(String username);
}
