package com.ceos20.instagram.post.controller;

import com.ceos20.instagram.post.dto.PostRequestDto;
import com.ceos20.instagram.post.dto.PostResponseDto;
import com.ceos20.instagram.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // 게시글 생성
    @PostMapping("/{userId}")   //로그인 구현 후 수정
    public ResponseEntity<Void> createPost(@RequestBody PostRequestDto postRequestDto, @PathVariable Long userId){
        postService.createPost(postRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 특정 유저의 전체 게시글 조회
    @GetMapping("/{userId}")    //로그인 구현 후 수정
    public ResponseEntity<List<PostResponseDto>> getAllPostsByUser(@PathVariable Long userId){
        List<PostResponseDto> dtos=postService.getAllPostsByUser(userId);
        return ResponseEntity.ok().body(dtos);
    }

    // 하나의 특정 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getOnePost(@PathVariable Long postId){
        PostResponseDto dto=postService.getOnePost(postId);
        return ResponseEntity.ok().body(dto);
    }
    
    // 팔로잉 중인 유저들의 게시글 전체 조회
    @GetMapping("/{userId}/followings")   //로그인 구현 후 수정
    public ResponseEntity<List<PostResponseDto>> getAllPostsByFollowing(@PathVariable Long userId){
        List<PostResponseDto> dtos=postService.getAllPostsByFollowing(userId);
        return ResponseEntity.ok().body(dtos);
    }

    // 특정 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto){
        PostResponseDto dto=postService.updatePost(postRequestDto, postId);
        return ResponseEntity.ok().body(dto);
    }

    //특정 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }
}
