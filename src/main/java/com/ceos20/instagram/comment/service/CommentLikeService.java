package com.ceos20.instagram.comment.service;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.comment.domain.CommentLike;
import com.ceos20.instagram.comment.repository.CommentLikeRepository;
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    //댓글 좋아요 누르기
    @Transactional
    public void pressCommentLike(Long commentId, Long userId) {
        Comment target=commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        User user=userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        CommentLike commentLike=commentLikeRepository.findByCommentId(commentId).orElse(null);

        if(commentLike==null) {// 좋아요 누른적 없음 -> 등록하기
            increaseCommentLike(user, target);
        }else{ //이미 좋아요 누름 -> 삭제하기
            decreaseCommentLike(commentLike, target);
        }

    }

    private void increaseCommentLike(User user, Comment target) {
        CommentLike newLike=CommentLike.builder()
                .user(user)
                .comment(target)
                .build();
        commentLikeRepository.save(newLike);

        target.increaseLike();
    }

    private void decreaseCommentLike(CommentLike commentLike, Comment target) {
        commentLikeRepository.delete(commentLike);

        target.decreaseLike();

    }

}
