package com.ceos20.instagram.comment.repository;

import com.ceos20.instagram.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByPostId(Long postId);

    @Query("select c from Comment c join fetch c.user u join fetch c.post p where c.post.id = :postId and c.parent is null")
    List<Comment> findParentsByPostId(@Param("postId") Long postId);  //commentDto로 만들 때 getPost(), getUser()해야해서 fetch join

    @Query("select c from Comment c join fetch c.user u join fetch c.post p where c.parent.id = :parentCommentId")
    List<Comment> findChildsByParentId(@Param("parentCommentId") Long parentCommentId);
}
