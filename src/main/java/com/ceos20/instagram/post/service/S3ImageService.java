package com.ceos20.instagram.post.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ceos20.instagram.global.exception.BadRequestException;
import com.ceos20.instagram.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class S3ImageService {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    // 이미지파일 저장하고 url 반환
    @Async("ImageUploadExecutor") //지정한 쓰레드 풀에서 실행되도록 설정
    public CompletableFuture<String> saveImage(MultipartFile image) {
        return CompletableFuture.supplyAsync(()->{
            if (image.isEmpty()) {
                return null;
            }
            //확장자 명이 올바른지 확인 (파일 확장자가 jpg, jpeg, png, gif 중에 속하는지)
            validateFileExtension(image.getOriginalFilename());

            //파일 이름에 uuid를 붙여 unique하게 만들어줌
            String filename = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
            //String encodedFileName= URLEncoder.encode(filename, StandardCharsets.UTF_8);

            try {
                ObjectMetadata metadata = getObjectMetaData(image);

                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filename, image.getInputStream(), metadata).clone().withCannedAcl(
                        CannedAccessControlList.PublicRead);
                amazonS3.putObject(putObjectRequest);  //bucket에 저장된 파일의 url 경로 반환

            } catch (IOException e) {
                throw new RuntimeException("이미지를 s3에 업로드 하는 중에 문제 발생", e);
            }

            return amazonS3.getUrl(bucketName, filename).toString();

            //return "Temp post image url";
        }).exceptionally(ex->{
            // 비동기 작업 중 예외 발생한 경우를 처리
            throw new RuntimeException("S3 이미지 업로드 실패", ex);
        });

    }

    private void validateFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new BadRequestException(ExceptionCode.NO_FILENAME);
        }

        int lastDotIndex = filename.lastIndexOf(".");
        String extension = filename.substring(lastDotIndex + 1);

        List<String> extensionList = Arrays.asList("jpg", "jpeg", "png", "gif", "JPG", "JPEG", "PNG", "GIF");

        if (!extensionList.contains(extension)) {
            throw new BadRequestException(ExceptionCode.INVALID_EXTENSION);
        }
    }

    private ObjectMetadata getObjectMetaData(MultipartFile image) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());
        metadata.setContentType(image.getContentType());

        return metadata;
    }
}

