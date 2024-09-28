package com.ceos20.instagram.dm.repository;

import com.ceos20.instagram.dm.domain.DmRoom;
import com.ceos20.instagram.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DmRoomRepository extends JpaRepository<DmRoom, Long> {
    Optional<DmRoom> findByUser1AndUser2(User sender, User receiver);

    @Query("select r from DmRoom r join fetch r.user1 join fetch r.user2 where r.user1.id = :userId or r.user2.id = :userId order by r.updatedAt desc")
    List<DmRoom> findRoomsByUser1IdOrUser2Id(@Param("userId")Long userId);  // dto 만들때 r.getUser2()해서 fetch join
}
