package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class RelationshipDto {
    private Long relationshipId;
    private UserDto sourceUser;
    private UserDto targetUser;
    private String relationshipName;
}
