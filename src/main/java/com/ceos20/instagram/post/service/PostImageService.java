package com.ceos20.instagram.post.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ceos20.instagram.global.exception.BadRequestException;
import com.ceos20.instagram.global.exception.ExceptionCode;
import com.ceos20.instagram.global.exception.S3Exception;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.post.repository.PostImageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

//@slf4j 이용해서 로그 찍도록 코드 짜기
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final AmazonS3 amazonS3;
    private final S3ImageService s3ImageService;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucketUrl}")
    private String bucketUrl;


    @Transactional
    public List<PostImage> changeToPostImage(List<MultipartFile> images, Post post, int maxOrder) {

        if(images.isEmpty()){ //@ModelAttribute로 프론트한테 받을 시 List<MultipartFile>가 누락되어 있으면 스프링이 자동으로 빈 리스트로 처리한다.
            return new ArrayList<>();
        }

        // 비동기적으로 이미지 업로드 작업 수행
        List<CompletableFuture<PostImage>> futures = new ArrayList<>();
        for(int i=0;i<images.size();i++){
            MultipartFile image = images.get(i);
            int order=maxOrder+i+1;

            CompletableFuture<PostImage> future=s3ImageService.saveImage(image)
                    .thenApply(url -> PostImage.builder()
                            .postImageurl(url)
                            .originalFileName(image.getOriginalFilename())
                            .post(post)
                            .imageOrder(order)  // 순서 저장 -> 나중에 조회할 때 order ASC로 보여지게 하기
                            .build());

            futures.add(future);
        }

        // 모든 업로드 작업이 완료될 때까지 기다리고 실행
        CompletableFuture<List<PostImage>> allImagesFuture=CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v->futures.stream()
                        .map(CompletableFuture::join)
                        .toList());

        return allImagesFuture.join();


    }




    //s3에 업로드한 이미지 s3에서 삭제(여기서는 db에서 삭제 안함)
    @Transactional
    public void deleteAllImages(Long postId) {
        List<PostImage> images=postImageRepository.findAllPostImageByPostId(postId);
        for(PostImage image:images){
            if(image.getPostImageurl()!=null){
                deleteImage(image.getPostImageurl());
            }
        }
    }
    //하나의 이미지 s3에서 삭제
    private void deleteImage(String postImageUrl){
        //s3에서 삭제
        try{
            //존재하는지 확인하고나서 지우기
            int idx=postImageUrl.indexOf(bucketUrl);
            String filenameWithUuid=postImageUrl.substring(idx+bucketUrl.length()+1);

            //System.out.println("대상"+filenameWithUuid);

            //디코딩을 해야 한글 및 공백이 포함된 파일명이 깨지지 않은채 가져와짐
            String decodedFileName=URLDecoder.decode(filenameWithUuid, StandardCharsets.UTF_8);

            if(amazonS3.doesObjectExist(bucketName, decodedFileName)){
                DeleteObjectRequest deleteObjectRequest=new DeleteObjectRequest(bucketName, decodedFileName);
                amazonS3.deleteObject(deleteObjectRequest);
            }
            else{
                System.out.println("대상없음");
            }
        } catch (S3Exception e){
            throw new RuntimeException("s3에서 이미지삭제 실패", e);
        }
    }



    //게시글 수정 시, 삭제된 이미지 s3, db에서 삭제하기
    @Transactional
    public List<PostImage> deleteImagesUpdatePost(List<PostImage> currentImages, List<MultipartFile> newImages) { //(현재 게시글에 저장되어있는 이미지, 새로 업데이트 누른 순간의 이미지

        List<String> newImageNames=newImages.stream()
                .map(MultipartFile::getOriginalFilename)
                .toList();

        List<PostImage> imagesToDelete=currentImages.stream()
                .filter(postImage -> !newImageNames.contains(postImage.getOriginalFileName()))
                .toList();

        for(PostImage postImage:imagesToDelete){
            if(postImage.getPostImageurl()!=null){
                deleteImage(postImage.getPostImageurl());  //s3에서 이미지 삭제
            }
            postImageRepository.delete(postImage);
        }
        return imagesToDelete;
    }


    //게시글 수정 시, 새로 추가되어야 하는 이미지 리스트 반환
    @Transactional
    public List<MultipartFile> saveImagesUpdatePost(List<PostImage> currentImages, List<MultipartFile> newImages){
        List<String> currentImageNames=currentImages.stream()
                .map(PostImage::getOriginalFileName)
                .toList();

        List<MultipartFile> imagesToAdd=newImages.stream()
                .filter(newImage->!currentImageNames.contains(newImage.getOriginalFilename()))
                .toList();

        return imagesToAdd;
    }

    @Transactional
    public void saveImagesToDb(List<PostImage> images) {
        postImageRepository.saveAll(images);
    }
}