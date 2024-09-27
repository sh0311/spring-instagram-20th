package com.ceos20.instagram.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String content;
    private Long postId;
    private Long userId;
    private Long parentId;
}
