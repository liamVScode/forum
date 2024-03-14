package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.MessageDto;
import com.example.foruminforexchange.model.Chat;
import com.example.foruminforexchange.model.Message;
import com.example.foruminforexchange.service.ChatService;
import com.example.foruminforexchange.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat/{chatId}")
    public void chat(@DestinationVariable Long chatId, MessageDto messageDto){
        messageDto.setChatId(chatService.createAndOrGetChat(chatId));

        MessageDto savedMessageDto = messageService.addNewMessage(messageDto);
        simpMessagingTemplate.convertAndSend("/topic/" + chatId, savedMessageDto);
    }

    @GetMapping("api/chat/{chatId}")
    public ResponseEntity<List<MessageDto>> getMessageByChatId(@PathVariable Long chatId){
        List<MessageDto> messages = chatService.getMessageByChatId(chatId);
        return ResponseEntity.ok(messages);
    }

}
