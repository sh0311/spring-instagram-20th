package com.ceos20.instagram.post.service;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.post.repository.PostImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;
    
    //이미지 업로드 및 저장방법 공부 후 수정 예정
    //게시물 업로드 시 업로드한 이미지 저장
    @Transactional
    public void saveImages(List<PostImage> images){
        for(PostImage image:images){
            postImageRepository.saveAll(images);
        }
    }

    public List<PostImage> changeToPostImage(List<MultipartFile> images, Post post) {
        return images.stream()
                .map(image -> {
                    String url=saveImage(image);
                    return PostImage.builder()
                            .postImageurl(url)
                            .post(post)  //연관관계의 주인인 postImage를 post와 연관관계를 설정해줘야 postImage에 post의 id가 외래키로 제대로 저장됨
                            .build();

                })
                .toList();
    }


    // 이미지파일 저장하고 url 반환 (공부하고 나서 나중에 구현)
    public String saveImage(MultipartFile image) {
        return "Temp post image url";
    }

    //서버에 업로드한 이미지 삭제 (공부하고 나서 나중에 구현)
    public void deleteImages(Long postId) {

    }
}