package com.ceos20.instagram.dm.dto;

import com.ceos20.instagram.dm.domain.Message;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MessageResponseDto {
    private Long id;
    private String content;
    private LocalDateTime readAt;
    private Long senderId;


    public static MessageResponseDto from(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .readAt(message.getReadAt())
                .senderId(message.getSender().getId())
                .build();


    }
}
