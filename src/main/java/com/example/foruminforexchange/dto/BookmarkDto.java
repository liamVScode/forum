package com.example.foruminforexchange.dto;

import lombok.Data;

@Data
public class BookmarkDto {
    Long bookmarkId;
    UserDto user;
    PostDto post;
}
