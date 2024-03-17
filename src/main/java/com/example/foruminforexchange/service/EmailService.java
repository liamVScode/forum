package com.example.foruminforexchange.service;

public interface EmailService {
    void sendEmail(String to, String subject, String content);
}
