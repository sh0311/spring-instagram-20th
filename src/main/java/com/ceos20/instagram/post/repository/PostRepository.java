package com.ceos20.instagram.post.repository;

import com.ceos20.instagram.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
