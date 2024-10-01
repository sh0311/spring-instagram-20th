package com.ceos20.instagram.post.service;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.post.repository.PostImageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

//@slf4j 이용해서 로그 찍도록 코드 짜기
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
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
        if(images.isEmpty()){ //@RequestBody로 프론트한테 받을 시 List<MultipartFile>가 누락되어 있으면 스프링이 자동으로 빈 리스트로 처리한다.
            return new ArrayList<>();
        }
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
    @Transactional
    public String saveImage(MultipartFile image) {
        return "Temp post image url";
    }

    //서버에 업로드한 이미지 삭제 (공부하고 나서 나중에 구현)
    @Transactional
    public void deleteImages(Long postId) {

    }
}