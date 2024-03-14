package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.dto.MessageDto;
import com.example.foruminforexchange.mapper.MessageMapper;
import com.example.foruminforexchange.model.Chat;
import com.example.foruminforexchange.model.Message;
import com.example.foruminforexchange.repository.ChatRepo;
import com.example.foruminforexchange.repository.MessageRepo;
import com.example.foruminforexchange.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatServiceImpl implements ChatService {
    private ChatRepo chatRepo;
    private MessageRepo messageRepo;

    @Override
    public List<MessageDto> getMessageByChatId(Long chatId) {
        List<Message> messages = messageRepo.findByChatChatId(chatId);
        if(messages != null){
            return messages.stream().map((message) -> MessageMapper.mapToMessageDto(message)).collect(Collectors.toList());
        }
        else{
            return new ArrayList<MessageDto>();
        }
    }


    @Override
    public Long createAndOrGetChat(Long chatId) {
        Chat chatFind = chatRepo.findById(chatId).get();
        if(chatFind != null){
            return (chatFind.getChatId());
        }
        else {
            Chat chat = new Chat();
            return (chatRepo.save(chat).getChatId());
        }
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
