package com.ceos20.instagram.follow.controller;

import com.ceos20.instagram.follow.dto.FollowRequestDto;
import com.ceos20.instagram.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    //팔로우 신청하기
    @PostMapping
    public ResponseEntity<Void> createFollow(@RequestBody FollowRequestDto requestDto) {
        followService.createFollow(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //팔로우 승인하기
    @PatchMapping
    public ResponseEntity<Void> approveFollow(@RequestBody FollowRequestDto requestDto) {
        followService.approveFollow(requestDto);
        return ResponseEntity.ok().build();
    }


    //팔로잉 취소하기
    @DeleteMapping
    public ResponseEntity<Void> deleteFollow(@RequestBody FollowRequestDto requestDto) {
        followService.deleteFollowing((requestDto));
        return ResponseEntity.ok().build();
    }
}
