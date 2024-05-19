package com.example.foruminforexchange.service.impl;

import com.example.foruminforexchange.Exception.AppException;
import com.example.foruminforexchange.Exception.ErrorCode;
import com.example.foruminforexchange.dto.MessageDto;
import com.example.foruminforexchange.dto.MessageRequest;
import com.example.foruminforexchange.mapper.MessageMapper;
import com.example.foruminforexchange.model.Chat;
import com.example.foruminforexchange.model.Message;
import com.example.foruminforexchange.model.User;
import com.example.foruminforexchange.repository.ChatRepo;
import com.example.foruminforexchange.repository.MessageRepo;
import com.example.foruminforexchange.repository.UserRepo;
import com.example.foruminforexchange.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private MessageRepo messageRepo;
    private ChatRepo chatRepo;
    private UserRepo userRepo;
    @Override
    public MessageDto addNewMessage(MessageRequest messageRequest) {
        // Lấy thông tin chat từ chatId được cung cấp, nếu không tìm thấy thì ném ra ngoại lệ
        Chat chat = chatRepo.findById(messageRequest.getChatId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        chat.setLastMessageTime(LocalDateTime.now()); // Cập nhật thời gian tin nhắn cuối

        // Lấy thông tin người dùng từ userId được cung cấp, nếu không tìm thấy thì ném ra ngoại lệ
        User user = userRepo.findById(messageRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tạo mới một tin nhắn
        Message message = new Message();
        message.setUser(user); // Thiết lập người dùng cho tin nhắn
        message.setMessageContent(messageRequest.getMessageContent()); // Thiết lập nội dung tin nhắn
        message.setCreateAt(LocalDateTime.now()); // Thiết lập thời gian tạo
        message.setChat(chat); // Thiết lập chat cho tin nhắn

        // Lưu tin nhắn vào cơ sở dữ liệu
        Message savedMessage = messageRepo.save(message);
        chatRepo.save(chat); // Cập nhật thông tin chat

        // Chuyển đổi tin nhắn đã lưu thành DTO và trả về
        return MessageMapper.mapToMessageDto(savedMessage);
    }

}
