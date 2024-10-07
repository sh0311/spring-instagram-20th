package com.ceos20.instagram.post.controller;

import com.ceos20.instagram.post.dto.PostRequestDto;
import com.ceos20.instagram.post.dto.PostResponseDto;
import com.ceos20.instagram.post.service.PostImageService;
import com.ceos20.instagram.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name="Post Controller", description="게시글 컨트롤러")
public class PostController {
    private final PostService postService;
    private final PostImageService postImageService;

    // 게시글 생성
    @PostMapping(value="/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)   //로그인 구현 후 수정   //Swagger에서 MultipartFile을 받게 하기 위해
    @Operation(summary="게시글 생성", description="새 게시글 생성")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="게시글 생성 성공"),
            @ApiResponse(responseCode="400", description="게시글 생성 실패")
    })
    @Parameters({
            @Parameter(name = "userId",description = "게시글 생성할 유저의 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<Void> createPost(@ModelAttribute PostRequestDto postRequestDto, @PathVariable Long userId){
        postService.createPost(postRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 특정 유저의 전체 게시글 조회
    @GetMapping("/users/{userId}")    //로그인 구현 후 수정
    @Operation(summary="유저의 게시글 조회", description="특정 유저의 전체 게시글 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="게시글 조회 성공"),
            @ApiResponse(responseCode="404", description="해당 id 유저 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "userId",description = "게시글 조회할 유저의 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<List<PostResponseDto>> getAllPostsByUser(@PathVariable Long userId){
        List<PostResponseDto> dtos=postService.getAllPostsByUser(userId);
        return ResponseEntity.ok().body(dtos);
    }

    // 하나의 특정 게시글 조회
    @GetMapping("/{postId}")
    @Operation(summary="특정 게시글 조회", description="특정 id의 게시글 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="게시글 조회 성공"),
            @ApiResponse(responseCode="404", description="해당 id의 게시글이 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "postId",description = "조회할 게시글의 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<PostResponseDto> getOnePost(@PathVariable Long postId){
        PostResponseDto dto=postService.getOnePost(postId);
        return ResponseEntity.ok().body(dto);
    }
    
    // 팔로잉 중인 유저들의 게시글 전체 조회
    @GetMapping("/{userId}/followings")   //로그인 구현 후 수정
    @Operation(summary="팔로잉 게시글 조회", description="현재 팔로잉하는 사람들의 전체 게시글 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="게시글 조회 성공"),
            @ApiResponse(responseCode="404", description="해당 id 유저 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "userId",description = "현재 조회하려는 유저의 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<List<PostResponseDto>> getAllPostsByFollowing(@PathVariable Long userId){
        List<PostResponseDto> dtos=postService.getAllPostsByFollowing(userId);
        return ResponseEntity.ok().body(dtos);
    }

    // 특정 게시글 수정
    @PutMapping(value="/{postId}/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)   //로그인 구현 후 수정
    @Operation(summary="게시글 수정", description="특정 게시글 수정")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="게시글 수정 성공"),
            @ApiResponse(responseCode="404", description="해당 id 유저/게시글이 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "userId",description = "유저 id", in = ParameterIn.PATH ,required = true),
            @Parameter(name = "postId",description = "게시글 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @PathVariable Long userId, @ModelAttribute PostRequestDto postRequestDto){
        PostResponseDto dto=postService.updatePost(postId, userId, postRequestDto);
        return ResponseEntity.ok().body(dto);
    }

    //특정 게시글 삭제
    @DeleteMapping("/{postId}")
    @Operation(summary="게시글 삭제", description="특정 id의 게시글 삭제")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="게시글 삭제 성공"),
            @ApiResponse(responseCode="404", description="해당 id의 게시글이 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "postId",description = "삭제할 게시글의 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<Void> deletePost(@PathVariable Long postId){
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }


}
