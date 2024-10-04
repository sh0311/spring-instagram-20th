package com.ceos20.instagram.comment.dto;

import com.ceos20.instagram.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private int likeNum;
    private String content;
    private Long postId;
    private Long userId;

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .likeNum(comment.getLikeNum())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .userId(comment.getUser().getId())
                .build();


    }
}
