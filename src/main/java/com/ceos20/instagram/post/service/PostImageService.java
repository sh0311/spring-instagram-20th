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

    @Transactional
    public List<PostImage> changeToPostImage(List<MultipartFile> images, Post post) {

        if(images.isEmpty()){ //@ModelAttribute로 프론트한테 받을 시 List<MultipartFile>가 누락되어 있으면 스프링이 자동으로 빈 리스트로 처리한다.
            return new ArrayList<>();
        }
        List<PostImage> newImages= images.stream()
                .map(image -> {
                    String url=saveImage(image);
                    return PostImage.builder()
                            .postImageurl(url)
                            .post(post)  //연관관계의 주인인 postImage를 post와 연관관계를 설정해줘야 postImage에 post의 id가 외래키로 제대로 저장됨
                            .build();

                })
                .toList();

        return newImages;

    }


    // 이미지파일 저장하고 url 반환 (공부하고 나서 나중에 구현)
    private String saveImage(MultipartFile image) {
        if(image.isEmpty()){
            return null;
        }
        return "Temp post image url";
    }

    //서버에 업로드한 이미지 삭제 (공부하고 나서 나중에 구현)
    @Transactional
    public void deleteAllImages(Long postId) {

    }
    //하나의 이미지 삭제
    @Transactional
    public void deleteImage(PostImage postImage){
        //s3에서 삭제


        postImageRepository.delete(postImage);
    }

    /*

    //게시글 수정 시, 삭제된 이미지 s3, db에서 삭제하기
    @Transactional
    public void deleteImagesUpdatePost(List<PostImage> currentImages, List<MultipartFile> newImages) {

        List<String> newImageNames=newImages.stream()
                .map(MultipartFile::getOriginalFilename)
                .toList();

        List<PostImage> imagesToDelete=currentImages.stream()
                .filter(postImage -> !newImageNames.contains(extractFileNameFromUrl(postImage.getPostImageurl())))
                .toList();

        for(PostImage postImage:imagesToDelete){
            deleteImage(postImage);
        }
    }

    private String extractFileNameFromUrl(String postImageurl) {
        return postImageurl.substring(postImageurl.lastIndexOf("/") + 1);  // URL에서 파일 이름만 추출
    }

    //게시글 수정 시, 추가된 이미지 s3, db에 저장하기
    @Transactional
    public void saveImagesUpdatePost(List<PostImage> currentImages, List<MultipartFile> newImages){

    }
*/
}