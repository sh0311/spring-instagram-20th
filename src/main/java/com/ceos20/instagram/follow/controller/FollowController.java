package com.ceos20.instagram.follow.controller;

import com.ceos20.instagram.follow.dto.FollowRequestDto;
import com.ceos20.instagram.follow.service.FollowService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
@Tag(name="Follow Controller", description="팔로우 컨트롤러")
public class FollowController {
    private final FollowService followService;

    //팔로우 신청하기
    @PostMapping
    @Operation(summary="팔로우 신청", description="팔로우 신청하기")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="팔로우 신청 성공"), @ApiResponse(responseCode="404", description="해당 id 댓글 찾기 실패")
    })
    public ResponseEntity<Void> createFollow(@RequestBody FollowRequestDto requestDto) {
        followService.createFollow(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //팔로우 승인하기
    @PatchMapping
    @Operation(summary="팔로우 승인", description="팔로우 승인하기")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="팔로우 승인 성공"),
    })
    public ResponseEntity<Void> approveFollow(@RequestBody FollowRequestDto requestDto) {
        followService.approveFollow(requestDto);
        return ResponseEntity.ok().build();
    }


    //팔로잉 취소하기
    @DeleteMapping
    @Operation(summary="팔로잉 취소", description="팔로잉 취소하기")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="팔로잉 취소 성공"),
    })
    public ResponseEntity<Void> deleteFollow(@RequestBody FollowRequestDto requestDto) {
        followService.deleteFollowing((requestDto));
        return ResponseEntity.ok().build();
    }
}
