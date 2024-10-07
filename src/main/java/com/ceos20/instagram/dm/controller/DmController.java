package com.ceos20.instagram.dm.controller;

import com.ceos20.instagram.dm.dto.DmRoomResponseDto;
import com.ceos20.instagram.dm.dto.MessageRequestDto;
import com.ceos20.instagram.dm.dto.MessageResponseDto;
import com.ceos20.instagram.dm.service.DmService;
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
@Tag(name="Dm Controller", description="디엠 컨트롤러")
public class DmController {

    private final DmService dmService;

    // dm 보내기
    @PostMapping("/messages")
    @Operation(summary="dm 보내기", description="dm 보내기")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="dm 보내기 성공"),
    })
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDto messageRequestDto){
        dmService.sendMessage(messageRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 채팅방 나가기
    @PatchMapping("/dmRooms/{roomId}/{userId}")  // 로그인 구현 후 @AuthenticationPrincipal
    @Operation(summary="채팅방 나가기", description="채팅방 나가기")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="채팅방 나가기 성공"),
    })
    @Parameters({
            @Parameter(name = "roomId",description = "채팅방 id", in = ParameterIn.PATH ,required = true),
            @Parameter(name = "userId",description = "채팅방 나가려하는 유저의 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId, @PathVariable Long userId){
        dmService.leaveRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }

    // 최근 대화 오간순으로 내 채팅방 리스트 반환
    @GetMapping("/dmRooms/{userId}")   // 로그인 구현 후 @AuthenticationPrincipal
    @Operation(summary="최근 대화 오간순으로 내 채팅방 리스트 조회", description="최근 대화 오간순으로 내 채팅방 리스트 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="채팅방 리스트 조회 성공"),
    })
    @Parameters({
            @Parameter(name = "userId",description = "내 채팅방 조회하려는 유저 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<List<DmRoomResponseDto>> getMyAllRooms(@PathVariable Long userId){
        List<DmRoomResponseDto> myRooms=dmService.getMyAllRooms(userId);
        return ResponseEntity.ok(myRooms);
    }

    // 내가 보낸 dm삭제
    @DeleteMapping("/messages/{messageId}")
    @Operation(summary="내가 보낸 dm 삭제", description="내가 보낸 dm 삭제")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="dm 삭제 성공"),
    })
    @Parameters({
            @Parameter(name = "messageId",description = "삭제하려는 메시지의 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId){
        dmService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    // 특정 dmRoom에 있는 메시지들 조회
    @GetMapping("/dmRooms/{roomId}/{userId}")   // 로그인 구현 후 @AuthenticationPrincipal
    @Operation(summary="채팅방 메시지 조회", description="특정 dmRoom에 있는 메시지들 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode="200", description="채팅방 내 메시지 조회 성공"),
    })
    @Parameters({
            @Parameter(name = "roomId",description = "조회하려는 채팅방 id", in = ParameterIn.PATH ,required = true),
            @Parameter(name = "userId",description = "현재 로그인한 유저 id", in = ParameterIn.PATH ,required = true),
    })
    public ResponseEntity<List<MessageResponseDto>> getMessagesInRooms(@PathVariable Long roomId, @PathVariable Long userId){
        List<MessageResponseDto> messages=dmService.getMessagesInRoom(roomId, userId);
        return ResponseEntity.ok().body(messages);
    }
}
