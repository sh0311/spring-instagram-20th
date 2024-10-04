package com.ceos20.instagram.comment.controller;

import com.ceos20.instagram.comment.dto.CommentRequestDto;
import com.ceos20.instagram.comment.dto.CommentResponseDto;
import com.ceos20.instagram.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {
    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/comments")
    public ResponseEntity<Void> createComment(@RequestBody CommentRequestDto commentRequestDto){
        commentService.createComment(commentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();  //생성 성공 -> 201 상태코드를 응답으로
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    //부모댓글 조회
    @GetMapping("/{postId}/parents")
    public ResponseEntity<List<CommentResponseDto>> getParentComments(@PathVariable Long postId){
        List<CommentResponseDto> response=commentService.getParentCommentsByPost(postId);
        return ResponseEntity.ok().body(response);
    }

    // 특정 부모의 자식 댓글 조회
    @GetMapping("/comments/{parentId}/childs")
    public ResponseEntity<List<CommentResponseDto>> getChildCommentsByParent(@PathVariable Long parentId){
        List<CommentResponseDto> response=commentService.getChildCommentsByParent(parentId);
        return ResponseEntity.ok().body(response);
    }
}
