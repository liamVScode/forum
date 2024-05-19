package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUserUserId(Long userId, Pageable pageable);
}
