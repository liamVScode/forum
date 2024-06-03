package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.RelationshipDto;
import com.example.foruminforexchange.model.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelationshipMapper {
    @Autowired
    private UserMapper userMapper;
    public RelationshipDto convertToRelationshipDto(Relationship relationship){
        RelationshipDto relationshipDto = new RelationshipDto();
        relationshipDto.setRelationshipId(relationship.getRelationshipId());
        relationshipDto.setRelationshipName(relationship.getRelationshipName());
        relationshipDto.setSourceUser(userMapper.convertToUserDto(relationship.getSourceUser()));
        relationshipDto.setTargetUser(userMapper.convertToUserDto(relationship.getTargetUser()));
        return relationshipDto;
    }
}
