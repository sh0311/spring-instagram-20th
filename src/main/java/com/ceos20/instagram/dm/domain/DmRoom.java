package com.ceos20.instagram.dm.domain;

import com.ceos20.instagram.global.BaseTimeEntity;
import com.ceos20.instagram.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Slf4j
public class DmRoom extends BaseTimeEntity { //최근에 채팅이 오거나 보낸 채팅방 순으로 저열되어야 하므로 updated_at 필요함
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user1_id")
    private User user1;  //처음 메시지보낸사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user2_id")
    private User user2;  //처음 메시지 받은사람

    // 유저가 채팅방 떠난 시간 이후의 메시지만 보여줘야 해서
    private LocalDateTime user1LeaveTime;
    private LocalDateTime user2LeaveTime;


    public void updateLastActivity() {
        updateUpdatedAt();
    }

    public boolean isUserInRoom(Long userId){
        log.debug("room id: "+id);
        log.debug("user1 id: "+user1.getId());
        log.debug("user2 id: "+user2.getId());
        return (user1!=null && userId.equals(this.user1.getId())||user2!=null && userId.equals(this.user2.getId()));
    }

    public void updateLeaveTime(Long userId){
        if (userId.equals(user1.getId())) {
            user1LeaveTime=LocalDateTime.now();
        }
        else if(userId.equals(user2.getId())) {
            user2LeaveTime=LocalDateTime.now();
        }
    }
}
