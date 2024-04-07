package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Prefix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrefixRepo extends JpaRepository<Prefix, Long> {
    Prefix findPrefixByPrefixId(Long id);

    @Query("SELECT p FROM Prefix p JOIN p.topicPrefixes tp WHERE tp.topic.topicId = :topicId")
    List<Prefix> findPrefixesByTopicId(@Param("topicId") Long topicId);

}
