package com.ceos20.instagram.dm.dto;

import com.ceos20.instagram.dm.domain.DmRoom;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DmRoomResponseDto {
    private Long id;
    private String user2Nickname; //상대방닉네임

    public static DmRoomResponseDto of(DmRoom room,String otherUserNickname){
        return DmRoomResponseDto.builder()
                .id(room.getId())
                .user2Nickname(otherUserNickname)
                .build();
    }
}
