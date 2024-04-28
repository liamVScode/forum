package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicRepo extends JpaRepository<Topic, Long> {

}
