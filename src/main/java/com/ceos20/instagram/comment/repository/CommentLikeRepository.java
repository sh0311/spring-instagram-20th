package com.ceos20.instagram.comment.repository;

import com.ceos20.instagram.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
