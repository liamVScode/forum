package com.example.foruminforexchange.mapper;

import com.example.foruminforexchange.dto.CommentDto;
import com.example.foruminforexchange.dto.LikeDto;
import com.example.foruminforexchange.model.Comment;
import com.example.foruminforexchange.model.ImageComment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDto convertToCommentDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentId(comment.getCommentId());
        commentDto.setPostId(comment.getPost().getPostId());
        commentDto.setCreateAt(comment.getCreateAt());
        commentDto.setContent(comment.getContent());
        if (comment.getImages() != null && !comment.getImages().isEmpty()) {
            List<String> imageUrls = comment.getImages().stream()
                    .map(ImageComment::getImageUrl)
                    .collect(Collectors.toList());
            commentDto.setImageUrls(imageUrls);
        }
        if (comment.getLikes() != null && !comment.getLikes().isEmpty()) {
            List<LikeDto> commentDtos = comment.getLikes().stream()
                    .map(like -> LikeMapper.convertToLikeDto(like))
                    .collect(Collectors.toList());
            commentDto.setLikeDtos(commentDtos);
        }
        commentDto.setUpdateAt(comment.getUpdateAt());
        commentDto.setUser(UserMapper.convertToUserDto(comment.getUser()));

        return commentDto;
    }
}
