package com.ceos20.instagram.dm.dto;

import com.ceos20.instagram.dm.domain.DmRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DmRoomResponseDto {
    private Long roomId;
    private String user2Nickname; //상대방닉네임

    public static DmRoomResponseDto of(DmRoom room,String otherUserNickname){
        return new DmRoomResponseDto(room.getId(), otherUserNickname);   //필드 수가 적고 모든 필드를 이용해 객체 만들어서 생성자 이용
    }
}
