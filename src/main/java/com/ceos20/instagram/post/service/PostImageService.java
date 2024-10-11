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

//@slf4j 이용해서 로그 찍도록 코드 짜기
@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucketUrl}")
    private String bucketUrl;


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
                            .originalFileName(image.getOriginalFilename())
                            .post(post)  //연관관계의 주인인 postImage를 post와 연관관계를 설정해줘야 postImage에 post의 id가 외래키로 제대로 저장됨
                            .build();

                })
                .toList();

        return newImages;

    }

    // 이미지파일 저장하고 url 반환
    private String saveImage(MultipartFile image) {
        if(image.isEmpty()){
            return null;
        }
        //확장자 명이 올바른지 확인 (파일 확장자가 jpg, jpeg, png, gif 중에 속하는지)
        validateFileExtension(image.getOriginalFilename());

        //파일 이름에 uuid를 붙여 unique하게 만들어줌
        String filename= UUID.randomUUID().toString()+"-"+image.getOriginalFilename();
        //String encodedFileName= URLEncoder.encode(filename, StandardCharsets.UTF_8);

        try{
            ObjectMetadata metadata=getObjectMetaData(image);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, image.getInputStream(), metadata).clone().withCannedAcl(
                    CannedAccessControlList.PublicRead);
            amazonS3.putObject(putObjectRequest);  //bucket에 저장된 파일의 url 경로 반환

        } catch (IOException e){
            throw new RuntimeException("이미지를 s3에 업로드 하는 중에 문제 발생", e);
        }

        return amazonS3.getUrl(bucketName, filename).toString();

        //return "Temp post image url";
    }
    private void validateFileExtension(String filename) {
        if(filename==null || filename.isEmpty()){
            throw new BadRequestException(ExceptionCode.NO_FILENAME);
        }

        int lastDotIndex=filename.lastIndexOf(".");
        String extension=filename.substring(lastDotIndex+1);

        List<String> extensionList= Arrays.asList("jpg", "jpeg", "png", "gif");

        if(!extensionList.contains(extension)){
            throw new BadRequestException(ExceptionCode.INVALID_EXTENSION);
        }
    }
    private ObjectMetadata getObjectMetaData(MultipartFile image) {
        ObjectMetadata metadata=new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());

        return metadata;
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