package com.ceos20.instagram.user.controller;

import com.ceos20.instagram.user.dto.UserRequestDto;
import com.ceos20.instagram.user.dto.UserResponseDto;
import com.ceos20.instagram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //user 한 명 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId){
        UserResponseDto dto=userService.getUser(userId);
        return ResponseEntity.ok().body(dto);
    }

    //user 정보 수정
    @PutMapping("/{userId}")  //로그인 구현 후 수정
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserRequestDto userRequestDto){
        UserResponseDto dto=userService.updateUser(userRequestDto, userId);
        return ResponseEntity.ok().body(dto);
    }

}
