package com.ceos20.instagram.follow.repository;

import com.ceos20.instagram.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
