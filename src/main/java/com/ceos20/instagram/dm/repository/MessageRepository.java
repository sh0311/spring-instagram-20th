package com.ceos20.instagram.dm.repository;

import com.ceos20.instagram.dm.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query("select m from Message m join fetch m.sender where m.room.id = :roomId")  //responseDto로 만들때 message.getSender().id 거챠야 해서
    List<Message> findMessageWithSenderByRoomId(@Param("roomId") Long roomId);

    @Query("select m from Message m join fetch m.sender where m.room.id = :roomId and m.createdAt > :userLeaveTime")  //responseDto로 만들때 message.getSender().id 거챠야 해서
    List<Message> findMessageWithSenderByRoomIdAndCreatedAtAfter(@Param("roomId") Long roomId, @Param("userLeaveTime") LocalDateTime userLeaveTime);
}
