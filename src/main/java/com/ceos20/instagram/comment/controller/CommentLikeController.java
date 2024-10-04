package com.ceos20.instagram.comment.controller;

import com.ceos20.instagram.comment.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/comments")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    //댓글 좋아요 누르기
    @PostMapping("/{commentId}/likes/{userId}")   //로그인 구현하고나서 userId는 @AuthenticationPrincipal 이용하기
    public ResponseEntity<Void> pressCommentLike(@PathVariable Long commentId, @PathVariable Long userId) {
        commentLikeService.pressCommentLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
