package com.ceos20.instagram.dm.dto;

import com.ceos20.instagram.dm.domain.DmRoom;
import com.ceos20.instagram.dm.domain.Message;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.service.UserService;
import lombok.Getter;

@Getter
public class MessageRequestDto {   //Dto에는 되도록 간단한 내용들 담기(user 대신 user_id)
    private String content;
    private Long senderId;
    private Long receiverId; //dmRoom이 생성되기 전일수도 있으니까 dmRoomId 대신 receiver의 id를 인자로 해서 채팅방 있나 확인

    public Message toEntity(MessageRequestDto messageRequestDto, DmRoom dmRoom, User sender) {

        return Message.builder()
                .content(messageRequestDto.getContent())
                .room(dmRoom)
                .sender(sender)
                .build();
    }
}
