package com.ceos20.instagram.post.dto;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostLike;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;

@Builder
public class PostLikeResponseDto {
    private Long id;
    private Long postId;
    private String userNickname;


    //dto -> entity
    public static PostLikeResponseDto of(PostLike like) {
        return PostLikeResponseDto.builder()
                .postId(like.getPost().getId())
                .userNickname(like.getUser().getNickname())
                .build();

    }
}
