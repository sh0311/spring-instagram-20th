package com.ceos20.instagram.dm.controller;

import com.ceos20.instagram.dm.dto.DmRoomResponseDto;
import com.ceos20.instagram.dm.dto.MessageRequestDto;
import com.ceos20.instagram.dm.dto.MessageResponseDto;
import com.ceos20.instagram.dm.service.DmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DmController {

    private final DmService dmService;

    // dm 보내기
    @PostMapping("/messages")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDto messageRequestDto){
        dmService.sendMessage(messageRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 채팅방 나가기
    @PatchMapping("/dmRooms/{roomId}/{userId}")  // 로그인 구현 후 @AuthenticationPrincipal
    public ResponseEntity<Void> leaveRoom(@PathVariable Long roomId, @PathVariable Long userId){
        dmService.leaveRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }

    // 최근 대화 오간순으로 내 채팅방 리스트 반환
    @GetMapping("/dmRooms/{userId}")   // 로그인 구현 후 @AuthenticationPrincipal
    public ResponseEntity<List<DmRoomResponseDto>> getMyAllRooms(@PathVariable Long userId){
        List<DmRoomResponseDto> myRooms=dmService.getMyAllRooms(userId);
        return ResponseEntity.ok(myRooms);
    }

    // 내가 보낸 dm삭제
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId){
        dmService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    // 특정 dmRoom에 있는 메시지들 조회
    @GetMapping("/dmRooms/{roomId}/{userId}")   // 로그인 구현 후 @AuthenticationPrincipal
    public ResponseEntity<List<MessageResponseDto>> getMessagesInRooms(@PathVariable Long roomId, @PathVariable Long userId){
        List<MessageResponseDto> messages=dmService.getMessagesInRoom(roomId, userId);
        return ResponseEntity.ok().body(messages);
    }
}
