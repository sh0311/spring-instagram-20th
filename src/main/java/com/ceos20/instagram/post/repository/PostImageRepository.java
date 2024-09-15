package com.ceos20.instagram.post.repository;

import com.ceos20.instagram.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
