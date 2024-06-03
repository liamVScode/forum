package com.example.foruminforexchange.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity(name="Chat")
@Table(name = "Chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_name")
    private String chatName;

    @Enumerated(EnumType.STRING)
    private ChatType chatType; //private or group

    private Long status;

    @Column(name = "receiver_status")
    private Long receiverStatus;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChat> members = new ArrayList<>();
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> messages;

    public Chat(){

    }

    public Chat(String chatName){
        this.chatName = chatName;
    }


    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public List<UserChat> getMembers() {
        return members;
    }

    public void setMembers(List<UserChat> members) {
        this.members = members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getReceiverStatus() {
        return receiverStatus;
    }

    public void setReceiverStatus(Long receiverStatus) {
        this.receiverStatus = receiverStatus;
    }
}
