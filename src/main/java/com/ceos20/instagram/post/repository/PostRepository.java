package com.ceos20.instagram.post.repository;

import com.ceos20.instagram.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select distinct p from Post p join fetch p.images where p.id = :postId")
    Optional<Post> findPostWithImageByPostId(@Param("postId") Long postId);

    @Query("select distinct p from Post p join fetch p.images where p.user.id = :userId")
    List<Post> findPostWithImageByUserId(@Param("userId") Long userId);

    // in 쿼리를 이용해 followingIds 리스트에 있는 id들을 모아 한번에 조회
    @Query("select distinct p from Post p join fetch p.images where p.user.id in :followingIds")
    List<Post> findPostsByUserIdsIn(@Param("followingIds") List<Long> followingIds);

    List<Post> findByUserId(Long userId);
}
