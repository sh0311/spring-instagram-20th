package com.ceos20.instagram.post.service;


import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.follow.repository.FollowRepository;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.post.dto.PostRequestDto;
import com.ceos20.instagram.post.dto.PostResponseDto;
import com.ceos20.instagram.post.repository.PostImageRepository;
import com.ceos20.instagram.post.repository.PostLikeRepository;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageService postImageService;
    private final FollowRepository followRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;

    // 게시글 생성
    @Transactional
    public void createPost(PostRequestDto postRequestDto,Long userId){

        //User 객체 가져오기
        User user=userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));
        //post 엔티티 생성, 저장
        Post newPost=postRequestDto.toPost(user);

        //MultipartFile을 PostImage로 변환
        List<PostImage> images=postImageService.changeToPostImage(postRequestDto.getImages(), newPost);

        //Post와 image 매핑
        newPost.mapImages(images);

        postRepository.save(newPost);
    }


    // 특정 유저의 전체 게시글 조회
    public List<PostResponseDto> getAllPostsByUser(Long userId){
        List<Post> userPosts=postRepository.findPostWithImageByUserId(userId);
        return userPosts.stream()
                .map(PostResponseDto::of)
                .toList();
    }


    // 하나의 특정 게시글 조회
    public PostResponseDto getOnePost(Long postId){
        // 해당 id의 게시글 찾기
        Post target=postRepository.findPostWithImageByPostId(postId).orElseThrow(()->new IllegalArgumentException("해당 id의 게시글이 존재하지 않습니다."));

        return PostResponseDto.of(target);
    }


    // 팔로잉 중인 유저들의 게시글 전체 조회
    public List<PostResponseDto> getAllPostsByFollowing(Long userId){
        List<Follow> followings=followRepository.findFollowingsByFollowerId(userId);
        //팔로잉 중인 유저들의 id 리스트
        List<Long> followingIds=followings.stream()
                .map(follow -> follow.getFollowing().getId())
                .toList();
        //해당 유저들의 게시글 리스트
        List<Post> postList = postRepository.findPostsByUserIdsIn(followingIds);
        return postList.stream()
                .map(PostResponseDto::of)
                .toList();
    }


    // 특정 게시글 수정
    @Transactional
    public PostResponseDto updatePost(PostRequestDto postRequestDto,Long userId){
        Post target=postRepository.findById(postRequestDto.getId()).orElseThrow(()-> new IllegalArgumentException("해당 id의 게시글이 없습니다."));
        if(!target.getUser().getId().equals(userId)){
            throw new IllegalStateException("게시글 작성자가 아닙니다.");
        }
        List<PostImage> images=postImageService.changeToPostImage(postRequestDto.getImages(), target);
        target.update(postRequestDto, images);
        postRepository.save(target);
        return PostResponseDto.of(target);
    }

    //특정 게시글 삭제
    @Transactional
    public void deletePost(Long postId){
        Post target=postRepository.findById(postId).orElseThrow(()-> new IllegalArgumentException("해당 id의 게시글이 없습니다."));
        commentRepository.deleteByPostId(postId);
        postLikeRepository.deleteByPostId(postId);
        postImageService.deleteImages(postId); //서버에 업로드한 이미지 삭제. db 말고. 구현예정

        //CascadeType.ALL에 의해 PostImage도 같이 삭제됨
        postRepository.delete(target);
    }
}
