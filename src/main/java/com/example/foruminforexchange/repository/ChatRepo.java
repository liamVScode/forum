package com.example.foruminforexchange.repository;

import com.example.foruminforexchange.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findByChatName(String chatName);
    @Query("SELECT c FROM Chat c JOIN c.members m WHERE :userId IN (SELECT m.userId FROM c.members m) " +
            "AND :otherUserId IN (SELECT m.userId FROM c.members m) AND SIZE(c.members) = 2" +
            "AND c.chatType = com.example.foruminforexchange.model.ChatType.PRIVATE")
    Chat findPrivateChatBetweenTwoUsers(@Param("userId") Long userId, @Param("otherUserId") Long otherUserId);

    @Query("SELECT c FROM Chat c JOIN c.members m WHERE m.userId = :userId")
    List<Chat> findAllChatsByUserId(@Param("userId") Long userId);

}
