package com.ceos20.instagram.post.dto;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;


@Getter
@Builder
public class PostRequestDto {
    private String content;
    private List<MultipartFile> images;  //클라이언트가 직접 파일을 업로드 할 때 스프링이 MultipartFile 클래스를 이용해 파일을 처리함

    //dto->entity
    public Post toEntity(User user){
        return Post.builder()
                .content(content)
                .user(user)
                .build();

    }
}
