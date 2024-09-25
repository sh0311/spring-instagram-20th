package com.ceos20.instagram.comment.repository;

import com.ceos20.instagram.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
