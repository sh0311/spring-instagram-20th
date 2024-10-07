package com.ceos20.instagram.post.service;


import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.comment.service.CommentService;
import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.follow.repository.FollowRepository;
import com.ceos20.instagram.follow.service.FollowService;
import com.ceos20.instagram.global.exception.ExceptionCode;
import com.ceos20.instagram.global.exception.ForbiddenException;
import com.ceos20.instagram.global.exception.NotFoundException;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.post.dto.PostRequestDto;
import com.ceos20.instagram.post.dto.PostResponseDto;
import com.ceos20.instagram.post.repository.PostImageRepository;
import com.ceos20.instagram.post.repository.PostLikeRepository;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;

import com.ceos20.instagram.user.service.UserService;
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

    private final UserService userService;
    private final PostImageService postImageService;
    private final FollowService followService;
    //순환참조 막기 위해 repository 사용
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;


    // 게시글 생성
    @Transactional
    public void createPost(PostRequestDto postRequestDto,Long userId){

        //User 객체 가져오기
        User user=userService.findUserById(userId);
        //post 엔티티 생성, 저장
        Post newPost=postRequestDto.toEntity(user);

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
                .map(PostResponseDto::from)
                .toList();
    }


    // 하나의 특정 게시글 조회
    public PostResponseDto getOnePost(Long postId){
        // 해당 id의 게시글 찾기
        Post target=postRepository.findPostWithImageByPostId(postId).orElseThrow(()->new NotFoundException(ExceptionCode.NOT_FOUND_POST));

        return PostResponseDto.from(target);
    }


    // 팔로잉 중인 유저들의 게시글 전체 조회
    public List<PostResponseDto> getAllPostsByFollowing(Long userId){
        List<Follow> followings=followService.findFollowingsByFollowerId(userId);
        //팔로잉 중인 유저들의 id 리스트
        List<Long> followingIds=followings.stream()
                .map(follow -> follow.getFollowing().getId())
                .toList();
        //해당 유저들의 게시글 리스트
        List<Post> postList = postRepository.findPostsByUserIdsIn(followingIds);
        return postList.stream()
                .map(PostResponseDto::from)
                .toList();
    }


    // 특정 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, Long userId, PostRequestDto postRequestDto){
        Post target=postRepository.findById(postId).orElseThrow(()-> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
        //게시글 작성자인지 체크
        if(!target.getUser().getId().equals(userId)){
            throw new ForbiddenException(ExceptionCode.NOT_POST_OWNER);
        }
        /*
        //삭제된 이미지 있다면 삭제
        postImageService.deleteImagesUpdatePost(target.getImages(), postRequestDto.getImages());
        //추가된 이미지 있다면 추가
        postImageService.saveImagesUpdatePost(target.getImages(), postRequestDto.getImages());
        */

        List<PostImage> images=postImageService.changeToPostImage(postRequestDto.getImages(), target);

        target.getImages().clear();
        target.update(postRequestDto, images);
        return PostResponseDto.from(target);
    }

    //특정 게시글 삭제
    @Transactional
    public void deletePost(Long postId){
        Post target=postRepository.findById(postId).orElseThrow(()-> new NotFoundException(ExceptionCode.NOT_FOUND_POST));
        commentRepository.deleteByPostId(postId);
        postLikeRepository.deleteByPostId(postId);
        postImageService.deleteAllImages(postId); //서버에 업로드한 이미지 삭제. db 말고. 구현예정

        //CascadeType.ALL에 의해 PostImage도 같이 삭제됨
        postRepository.delete(target);
    }

    public Post findPostById(Long postId){
        return postRepository.findById(postId).orElseThrow(()->new NotFoundException(ExceptionCode.NOT_FOUND_POST));
    }
}
