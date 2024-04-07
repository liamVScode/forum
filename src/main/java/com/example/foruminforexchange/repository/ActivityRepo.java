package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity, Long> {
    List<Activity> findAllByUserUserId(Long userId);
}
