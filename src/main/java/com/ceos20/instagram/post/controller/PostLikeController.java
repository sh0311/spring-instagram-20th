package com.ceos20.instagram.post.controller;

import com.ceos20.instagram.post.dto.PostLikeResponseDto;
import com.ceos20.instagram.post.service.PostLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("posts")
@Tag(name="PostLike Controller", description="게시글 좋아요 컨트롤러")
public class PostLikeController {
    private final PostLikeService postLikeService;

    //게시글에 좋아요 누르기
    @PostMapping("/{postId}/likes/{userId}")  //로그인 구현 후 수정
    @Operation(summary="게시글 좋아요 누르기", description="특정 게시글에 좋아요 버튼 누르기")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="게시글 좋아요 누르기 성공"),
            @ApiResponse(responseCode="404", description="해당 id의 게시글/유저가 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "postId",description = "게시글 id", in = ParameterIn.PATH ,required = true),
            @Parameter(name = "userId",description = "누르는 유저 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<Void> pressLike(@PathVariable Long postId, @PathVariable Long userId) {
        postLikeService.pressLike(postId, userId);
        return ResponseEntity.ok().build();
    }

    //특정 게시글 좋아요 전체 조회
    @GetMapping("/{postId}/likes")
    @Operation(summary="게시글 좋아요 목록 조회", description="특정 게시글 좋아요 목록 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="게시글 좋아요 조회 성공"),
            @ApiResponse(responseCode="404", description="해당 id의 게시글이 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "postId",description = "게시글 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<List<PostLikeResponseDto>> getPostLikeList(@PathVariable Long postId) {
        List<PostLikeResponseDto> dtos=postLikeService.getPostLikeList(postId);
        return ResponseEntity.ok().body(dtos);
    }
}
