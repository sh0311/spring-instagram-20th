package com.ceos20.instagram.post.dto;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostLike;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostLikeResponseDto {
    private Long id;
    private Long postId;
    private String userNickname;


    //entity -> dto
    public static PostLikeResponseDto from(PostLike like) {
        return PostLikeResponseDto.builder()
                .id(like.getId())
                .postId(like.getPost().getId())
                .userNickname(like.getUser().getNickname())
                .build();

    }
}
