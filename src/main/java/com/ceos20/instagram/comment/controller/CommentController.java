package com.ceos20.instagram.comment.controller;

import com.ceos20.instagram.comment.dto.CommentRequestDto;
import com.ceos20.instagram.comment.dto.CommentResponseDto;
import com.ceos20.instagram.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name="Comment Controller", description="댓글 컨트롤러")
public class CommentController {
    private final CommentService commentService;

    //댓글 등록
    @PostMapping("/comments")
    @Operation(summary="댓글 등록", description="게시글에 댓글 등록")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="댓글 등록 성공"),
            @ApiResponse(responseCode="400", description="댓글 등록 실패")
    })
    public ResponseEntity<Void> createComment(@RequestBody CommentRequestDto commentRequestDto){
        commentService.createComment(commentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();  //생성 성공 -> 201 상태코드를 응답으로
    }

    //댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary="댓글 삭제", description="특정 댓글 삭제")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="댓글 삭제 성공"),
            @ApiResponse(responseCode="404", description="해당 id 댓글 찾기 실패")
    })
    @Parameters({
            @Parameter(name = "commentId",description = "삭제할 댓글 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    //부모댓글 조회
    @GetMapping("/{postId}/parents")
    @Operation(summary="부모 댓글 조회", description="특정 게시글 모든 부모 댓글 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="부모 댓글 조회 성공"),
            @ApiResponse(responseCode="404", description="해당 id 부모댓글 찾기 실패")
    })
    @Parameters({
            @Parameter(name = "postId",description = "조회할 게시글 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<List<CommentResponseDto>> getParentComments(@PathVariable Long postId){
        List<CommentResponseDto> response=commentService.getParentCommentsByPost(postId);
        return ResponseEntity.ok().body(response);
    }

    // 특정 부모의 자식 댓글 조회
    @GetMapping("/comments/{parentId}/childs")
    @Operation(summary="자식 댓글 조회", description="특정 부모 자식 댓글 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="자식댓글 조회 성공"),
            @ApiResponse(responseCode="404", description="해당 id 부모댓글 찾기 실패")
    })
    @Parameters({
            @Parameter(name = "parentId",description = "조회할 부모댓글 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<List<CommentResponseDto>> getChildCommentsByParent(@PathVariable Long parentId){
        List<CommentResponseDto> response=commentService.getChildCommentsByParent(parentId);
        return ResponseEntity.ok().body(response);
    }
}
