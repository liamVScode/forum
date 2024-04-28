package com.example.foruminforexchange.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDto {
    Long reportId;
    UserDto userDto;
    Long postId;
    String reportContent;
    LocalDateTime createAt;
}
