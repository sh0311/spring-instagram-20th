package com.ceos20.instagram.comment.repository;

import com.ceos20.instagram.comment.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentId(Long commentId);

    @Modifying  //@Query와 함께 데이터베이스의 데이터 변경(삭제, 업데이트 등)을 수행하는 메서드에 사용해야 함
    @Query("delete from CommentLike c where c.comment.post.id in :postIds")
    void deleteByPostIdsIn(@Param("postIds")List<Long> postIds);
}
