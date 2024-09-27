package com.ceos20.instagram.post.repository;

import com.ceos20.instagram.post.domain.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteByPostId(Long postId);

    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    List<PostLike> findByPostId(Long postId);
}
