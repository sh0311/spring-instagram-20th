package com.ceos20.instagram.post.controller;

import com.ceos20.instagram.post.dto.PostLikeResponseDto;
import com.ceos20.instagram.post.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
public class PostLikeController {
    private final PostLikeService postLikeService;

    //게시글에 좋아요 누르기
    @PostMapping("/{postId}/likes/{userId}")  //로그인 구현 후 수정
    public ResponseEntity<Void> pressLike(@PathVariable Long postId, @PathVariable Long userId) {
        postLikeService.pressLike(postId, userId);
        return ResponseEntity.ok().build();
    }

    //특정 게시글 좋아요 전체 조회
    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<PostLikeResponseDto>> getPostLikeList(@PathVariable Long postId) {
        List<PostLikeResponseDto> dtos=postLikeService.getPostLikeList(postId);
        return ResponseEntity.ok().body(dtos);
    }
}
