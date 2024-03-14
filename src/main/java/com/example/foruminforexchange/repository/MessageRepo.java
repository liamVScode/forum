package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findByChatChatId(Long chatId);
}
