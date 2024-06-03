package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Relationship;
import com.example.foruminforexchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationshipRepo extends JpaRepository<Relationship, Long> {
    List<Relationship> findAllBySourceUser(User sourceUser);
    List<Relationship> findAllByTargetUser(User targetUser);
    Relationship findBySourceUserAndTargetUser(User sourceUser, User targetUser);

}
