package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findByChatName(String chatName);
}
