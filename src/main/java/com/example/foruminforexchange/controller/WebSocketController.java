package com.example.foruminforexchange.controller;

import com.example.foruminforexchange.dto.*;
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

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://localhost:4200"})
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat/{chatId}")
    public void chat(@DestinationVariable Long chatId, MessageRequest messageRequest){
        System.out.println(messageRequest.getMessageContent());
        System.out.println(chatId);
        MessageDto savedMessageDto = messageService.addNewMessage(messageRequest);
        simpMessagingTemplate.convertAndSend("/topic/" + chatId, savedMessageDto);
    }
    @GetMapping("api/chat/{chatId}")
    public ApiResponse<List<MessageDto>> getMessageByChatId(@PathVariable Long chatId){
        ApiResponse<List<MessageDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(chatService.getMessageByChatId(chatId));
        return apiResponse;
    }
    @PostMapping("api/chat/create-chat")
    public ApiResponse<ChatDto> createChat(@RequestBody CreateChatRequest createChatRequest){
        ApiResponse<ChatDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(chatService.createChat(createChatRequest));
        return apiResponse;
    }

    @GetMapping("api/chat/all-chat-by-user")
    public ApiResponse<List<ChatDto>> allChatByUser(){
        ApiResponse<List<ChatDto>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(chatService.getAllChatByCurrentUser());
        return apiResponse;
    }

    @GetMapping("api/chat/chat-with-user")
    public ApiResponse<ChatDto> getChatWithUser(@RequestParam Long userId){
        ApiResponse<ChatDto> apiResponse = new ApiResponse<>();
        apiResponse.setResult(chatService.getPrivateChatWithUser(userId));
        return apiResponse;
    }

}
