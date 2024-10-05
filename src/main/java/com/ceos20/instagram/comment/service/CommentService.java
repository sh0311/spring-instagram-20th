package com.ceos20.instagram.comment.service;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.comment.dto.CommentRequestDto;
import com.ceos20.instagram.comment.dto.CommentResponseDto;
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.global.exception.ExceptionCode;
import com.ceos20.instagram.global.exception.NotFoundException;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.post.service.PostService;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import com.ceos20.instagram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    //댓글 등록
    @Transactional
    public void createComment(CommentRequestDto commentRequestDto) {
        Post post=postService.findPostById(commentRequestDto.getPostId());
        User user=userService.findUserById(commentRequestDto.getUserId());
        //부모댓글 작성하는 경우
        Comment parent=null;
        //자식댓글 작성하는 경우
        if(commentRequestDto.getParentId()!=null) {
            parent=commentRepository.findById(commentRequestDto.getParentId()).orElseThrow(()-> new NotFoundException(ExceptionCode.NOT_FOUND_PARENT_COMMENT));
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
        Comment comment=commentRepository.findById(commentId).orElseThrow(()->new NotFoundException(ExceptionCode.NOT_FOUND_COMMENT));
        commentRepository.delete(comment);
    }

    //부모댓글 조회
    public List<CommentResponseDto> getParentCommentsByPost(Long postId){
        Post post=postService.findPostById(postId);

        List<Comment> parents=commentRepository.findParentsByPostId(postId);
        return parents.stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    //특정 부모댓글의 자식댓글 조회
    public List<CommentResponseDto> getChildCommentsByParent(Long parentCommentId){
        Comment parent=commentRepository.findById(parentCommentId).orElseThrow(()-> new NotFoundException(ExceptionCode.NOT_FOUND_PARENT_COMMENT));

        List<Comment> childs=commentRepository.findChildsByParentId(parentCommentId);
        return childs.stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()-> new NotFoundException(ExceptionCode.NOT_FOUND_COMMENT));
    }

    public void deleteCommentByPostId(Long postId) {
        commentRepository.deleteById(postId);
    }
}
