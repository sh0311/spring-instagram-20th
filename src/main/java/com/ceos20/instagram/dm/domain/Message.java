package com.ceos20.instagram.dm.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Message extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="message_id")
    private Long id;

    @NotNull
    @Column(columnDefinition = "text")
    private String content;

    @NotNull
    @Builder.Default
    private boolean isRead = false;

    //메시지 읽은 시간 저장
    private LocalDateTime readAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private DmRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sender_id")
    private User sender;

    public void setRead() {
        this.isRead=true;
        this.readAt = LocalDateTime.now();
    }
}
