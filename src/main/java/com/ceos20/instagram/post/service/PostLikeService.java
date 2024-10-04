package com.ceos20.instagram.post.service;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostLike;
import com.ceos20.instagram.post.dto.PostLikeResponseDto;
import com.ceos20.instagram.post.repository.PostLikeRepository;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;

import com.ceos20.instagram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {


    private final PostLikeRepository postLikeRepository;

    private final PostService postService;
    private final UserService userService;

    // 게시글에 좋아요 누르기
    @Transactional
    public void pressLike(Long postId, Long userId){
        Post target=postService.findPostById(postId);
        User user=userService.findUserById(userId);

        //repository에 해당 postId, userId 가진 객체가 있다면 좋아요 누른 적 있음, 없다면 없음
        PostLike like=postLikeRepository.findByPostIdAndUserId(postId, userId).orElse(null);
        //이미 좋아요 눌러져있다면 취소
        if(like==null){
            increaseLike(target, user);

        }else{//좋아요 누른적 없는 상태면 생성
            decreaseLike(target, like);
        }

    }

    // 게시글 좋아요 등록
    private void increaseLike(Post post, User user){
        PostLike like=PostLike.builder()
                .post(post)
                .user(user)
                .build();
        postLikeRepository.save(like);

        post.increaseLikeNum();
    }

    // 게시글 좋아요 취소
    private void decreaseLike(Post post, PostLike like){
        postLikeRepository.delete(like);

        post.decreaseLikeNum();
    }





    // 특정 게시글 좋아요 전체 조회
    public List<PostLikeResponseDto> getPostLikeList(Long postId){
        List<PostLike> likes=postLikeRepository.findByPostId(postId);
        List<PostLikeResponseDto> responseDtos=likes.stream()
                .map(PostLikeResponseDto::from)
                .toList();
        return responseDtos;
    }

    public void deletePostLikeByPostId(Long postId) {
        postLikeRepository.deleteByPostId(postId);
    }
}
