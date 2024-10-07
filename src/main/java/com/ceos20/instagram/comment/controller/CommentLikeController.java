package com.ceos20.instagram.comment.controller;

import com.ceos20.instagram.comment.service.CommentLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/comments")
@Tag(name="Comment Like Controller", description="댓글 좋아요 컨트롤러")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    //댓글 좋아요 누르기
    @PostMapping("/{commentId}/likes/{userId}")//로그인 구현하고나서 userId는 @AuthenticationPrincipal 이용하기
    @Operation(summary="댓글 좋아요 누르기", description="특정 댓글 좋아요 누르기")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="좋아요 누르기 성공"),
            @ApiResponse(responseCode="404", description="해당 id의 댓글/유저 찾기 실패")
    })
    @Parameters({
            @Parameter(name = "commentId",description = "좋아요 누를 댓글 id", in = ParameterIn.PATH ,required = true),
            @Parameter(name = "userId",description = "좋아요 누를 유저 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<Void> pressCommentLike(@PathVariable Long commentId, @PathVariable Long userId) {
        commentLikeService.pressCommentLike(commentId, userId);
        return ResponseEntity.ok().build();
    }
}
