package com.ceos20.instagram.post.dto;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostResponseDto {
    private Long id;
    private String content;
    private int likeNum;
    private String nickname;
    private List<String> imageUrlList;
    private LocalDateTime createdAt;

    //entity -> dto
    public static PostResponseDto from(Post post){
        List<String> sortedImageUrls=post.getImages().stream()
                .sorted(Comparator.comparingInt(PostImage::getImageOrder))
                .map(PostImage::getPostImageurl)
                .toList();

        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .likeNum(post.getLikeNum())
                .nickname(post.getUser().getNickname())
                .imageUrlList(sortedImageUrls)
                .createdAt(post.getCreatedAt())
                .build();


    }
}
