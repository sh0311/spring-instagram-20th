package com.ceos20.instagram.dm.repository;

import com.ceos20.instagram.dm.domain.DmRoom;
import com.ceos20.instagram.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DmRoomRepository extends JpaRepository<DmRoom, Long> {
    //sender와 receiver로 이루어진 채팅방 찾기. user1가 sender든 receiver든 상관없이 방 찾기
    @Query("select r from DmRoom r where (r.user1=:sender and r.user2=:receiver) or (r.user1=:receiver and r.user2=:sender)")
    Optional<DmRoom> findByUsers(@Param("sender") User sender, @Param("receiver") User receiver);

    @Query("select r from DmRoom r join fetch r.user1 join fetch r.user2 where r.user1.id = :userId or r.user2.id = :userId order by r.updatedAt desc")
    List<DmRoom> findRoomsByUserIdOrderByUpdatedAtDesc(@Param("userId")Long userId);  // dto 만들때 r.getUser2()해서 fetch join
}
