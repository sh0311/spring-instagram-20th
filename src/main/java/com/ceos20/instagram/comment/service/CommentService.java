package com.ceos20.instagram.comment.service;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.comment.dto.CommentRequestDto;
import com.ceos20.instagram.comment.dto.CommentResponseDto;
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    //댓글 등록
    @Transactional
    public void createComment(CommentRequestDto commentRequestDto) {
        Post post=postRepository.findById(commentRequestDto.getPostId()).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다."));
        User user=userRepository.findById(commentRequestDto.getUserId()).orElseThrow(()-> new IllegalArgumentException("해당 유저가 없습니다."));
        //부모댓글 작성하는 경우
        Comment parent=null;
        //자식댓글 작성하는 경우
        if(commentRequestDto.getParentId()!=null) {
            parent=commentRepository.findById(commentRequestDto.getParentId()).orElseThrow(()-> new IllegalArgumentException("해당 부모댓글이 존재하지 않습니다."));
        }
        
        Comment comment=Comment.builder()
                .content(commentRequestDto.getContent())
                .post(post)
                .user(user)
                .parent(parent)
                .build();

        commentRepository.save(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }

    //부모댓글 조회
    public List<CommentResponseDto> getParentCommentsByPost(Long postId){
        Post post=postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        List<Comment> parents=commentRepository.findParentsByPostId(postId);
        return parents.stream()
                .map(CommentResponseDto::of)
                .toList();
    }

    //특정 부모댓글의 자식댓글 조회
    public List<CommentResponseDto> getChildCommentsByParent(Long parentCommentId){
        Comment parent=commentRepository.findById(parentCommentId).orElseThrow(()-> new IllegalArgumentException("해당 부모댓글이 존재하지 않습니다."));

        List<Comment> childs=commentRepository.findChildsByParentId(parentCommentId);
        return childs.stream()
                .map(CommentResponseDto::of)
                .toList();
    }


}
