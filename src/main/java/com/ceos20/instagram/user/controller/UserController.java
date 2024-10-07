package com.ceos20.instagram.user.controller;

import com.ceos20.instagram.user.dto.UserRequestDto;
import com.ceos20.instagram.user.dto.UserResponseDto;
import com.ceos20.instagram.user.service.UserService;
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
@RequestMapping("/users")
@Tag(name="User Controller", description="유저 컨트롤러")
public class UserController {

    private final UserService userService;

    //user 생성
    @PostMapping
    @Operation(summary="유저 생성", description="유저 새로 생성")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="유저 생성 성공"),
            @ApiResponse(responseCode="400", description="유저 생성 실패")
    })
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        return ResponseEntity.ok().build();
    }


    //user 한 명 조회
    @GetMapping("/{userId}")
    @Operation(summary="특정 유저 조회", description="유저 id로 특정 유저 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="유저 조회 성공"),
            @ApiResponse(responseCode="404", description="해당 유저 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "userId",description = "조회할 유저 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId){
        UserResponseDto dto=userService.getUser(userId);
        return ResponseEntity.ok().body(dto);
    }

    //user 정보 수정
    @PutMapping("/{userId}")  //로그인 구현 후 수정
    @Operation(summary="유저 정보 수정", description="유저 id로 특정 유저 정보 수정")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="유저 정보 수정 성공"),
            @ApiResponse(responseCode="404", description="해당 유저 존재하지 않음")
    })
    @Parameters({
            @Parameter(name = "userId",description = "정보 수정할 유저 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long userId, @RequestBody UserRequestDto userRequestDto){
        UserResponseDto dto=userService.updateUser(userRequestDto, userId);
        return ResponseEntity.ok().body(dto);
    }

}
