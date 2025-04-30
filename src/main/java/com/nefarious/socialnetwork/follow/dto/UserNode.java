package com.nefarious.socialnetwork.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Node("User")
public class UserNode {
    @Id
    @GeneratedValue
    private UUID id;

    private String username;

    @Relationship(type = "FOLLOWS")
    private List<UserNode> following;
}
