package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.configuration.SecurityUtil;
import com.example.foruminforexchange.dto.ChatDto;
import com.example.foruminforexchange.dto.CreateChatRequest;
import com.example.foruminforexchange.dto.MessageDto;
import com.example.foruminforexchange.mapper.ChatMapper;
import com.example.foruminforexchange.mapper.MessageMapper;
import com.example.foruminforexchange.mapper.UserMapper;
import com.example.foruminforexchange.model.*;
import com.example.foruminforexchange.repository.ChatRepo;
import com.example.foruminforexchange.repository.MessageRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.ChatService;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private ChatRepo chatRepo;
    private MessageRepo messageRepo;
    private UserRepo userRepo;

    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<MessageDto> getMessageByChatId(Long chatId) {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        List<Message> messages = messageRepo.findByChatChatId(chatId);
        Chat chat = chatRepo.findById(chatId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        if(messages != null && !messages.isEmpty()){
            Message lastMessage = messages.get(messages.size() - 1);
            if (!lastMessage.getUser().getUserId().equals(user.getUserId())) {
                chat.setReceiverStatus(1L);
                chatRepo.save(chat);
            }
            return messages.stream().map((message) -> messageMapper.mapToMessageDto(message)).collect(Collectors.toList());
        }
        else{
            return null;
        }
    }

    @Override
    public ChatDto getPrivateChatWithUser(Long userId){
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        Chat chat = chatRepo.findPrivateChatBetweenTwoUsers(user.getUserId(), userId);
        if(chat == null){
            return null;
        }
        return chatMapper.convertToChatDto(chat);
    }

    @Override
    public List<ChatDto> getAllChatByCurrentUser() {
        String currentUserEmail = securityUtil.getCurrentUsername();
        if (currentUserEmail == null || "anonymousUser".equals(currentUserEmail)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userRepo.findUserByEmail(currentUserEmail);
        List<Chat> chats = chatRepo.findAllChatsByUserId(user.getUserId());
        chats.sort((chat1, chat2) -> {
            LocalDateTime lastMessageTime1 = chat1.getMessages().stream()
                    .map(Message::getCreateAt)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            LocalDateTime lastMessageTime2 = chat2.getMessages().stream()
                    .map(Message::getCreateAt)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            if (lastMessageTime1 == null && lastMessageTime2 == null) {
                return 0;
            } else if (lastMessageTime1 == null) {
                return 1;
            } else if (lastMessageTime2 == null) {
                return -1;
            } else {
                return lastMessageTime2.compareTo(lastMessageTime1);
            }
        });

        List<ChatDto> chatDtos = chats.stream().map(chat -> chatMapper.convertToChatDto(chat)).collect(Collectors.toList());
        return chatDtos;
    }


    @Override
    public ChatDto createChat(CreateChatRequest createChatRequest) {
        System.out.println(createChatRequest.getChatName() + "aaaaaa");
        List<UserChat> userChats = new ArrayList<>();
        User user = userRepo.findById(createChatRequest.getUserId().get(0)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Chat chat = new Chat();
        chat.setChatName(createChatRequest.getChatName() == null ? user.getFirstName() + user.getLastName() : createChatRequest.getChatName());
        if(createChatRequest.getChatType() == 0 || createChatRequest.getChatType() == null){
            chat.setChatType(ChatType.PRIVATE);
        }else {
            chat.setChatType(ChatType.GROUP);
        }
        for(Long userId : createChatRequest.getUserId()){
            User user1 = userRepo.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            UserChat userChat = new UserChat();
            userChat.setChat(chat);
            userChat.setUser(user1);
            userChats.add(userChat);
        }

        chat.setMembers(userChats);
        chatRepo.save(chat);
        return chatMapper.convertToChatDto(chat);
    }

    @Override
    public String generateTimeStamp() {
        //lay thoi gian hien tai
        Instant now = Instant.now();
        //lay mui gio thu GMT+7
        ZoneId zoneId = ZoneId.of("Asia/Bangkok");
        //chuyen sang localDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, zoneId);
        //format ngay
        String date = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(localDateTime);
        //format gio
        String time = DateTimeFormatter.ofPattern("HH:mm").format(localDateTime);

        String timeStamp = date + "-" + time;

        System.out.println("Timestamp: " + timeStamp);
        return timeStamp;
    }
}
