package com.ceos20.instagram.dm.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DmRoom extends BaseTimeEntity { //최근에 채팅이 오거나 보낸 채팅방 순으로 저열되어야 하므로 updated_at 필요함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user1_id")
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user2_id")
    private User user2;

    @OneToMany(mappedBy="room")
    private List<Message> messages = new ArrayList<>();

}
