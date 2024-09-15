package com.ceos20.instagram.post.repository;

import com.ceos20.instagram.post.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
