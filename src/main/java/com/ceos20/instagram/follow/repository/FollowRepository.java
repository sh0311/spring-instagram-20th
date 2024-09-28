package com.ceos20.instagram.follow.repository;

import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("select distinct f from Follow f left join fetch f.following where f.follower.id = :followerId")
    List<Follow> findFollowingsByFollowerId(@Param("followerId")Long followerId);

    Optional<Follow> findFollowByFollowingIdAndFollowerId(Long senderId, Long receiverId);
}
