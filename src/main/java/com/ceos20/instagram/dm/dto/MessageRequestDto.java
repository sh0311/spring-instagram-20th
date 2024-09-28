package com.ceos20.instagram.dm.dto;

import com.ceos20.instagram.dm.domain.DmRoom;
import com.ceos20.instagram.dm.domain.Message;
import com.ceos20.instagram.user.domain.User;
import lombok.Getter;

@Getter
public class MessageRequestDto {
    private String content;
    private User sender;
    private User receiver; //dmRoom이 생성되기 전일수도 있으니까 dmRoomId 대신 receiver의 id를 인자로 해서 채팅방 있나 확인

    public Message toMessage(MessageRequestDto messageRequestDto, DmRoom dmRoom) {
        return Message.builder()
                .content(messageRequestDto.getContent())
                .room(dmRoom)
                .sender(messageRequestDto.getSender())
                .build();
    }
}
