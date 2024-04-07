package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class FilterRequest {
    Long prefixId;
    String searchContent;
}
