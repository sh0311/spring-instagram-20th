package com.ceos20.instagram.Post;

import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.follow.repository.FollowRepository;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.post.dto.PostRequestDto;
import com.ceos20.instagram.post.dto.PostResponseDto;
import com.ceos20.instagram.post.repository.PostLikeRepository;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.post.service.PostImageService;
import com.ceos20.instagram.post.service.PostService;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)  //@Mock 사용하기 위해
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostImageService postImageService;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @InjectMocks
    private PostService postService;

    private User user;
    private User user2;
    private PostImage image1;
    private PostImage image2;
    private PostImage image3;
    private Post post1;
    private Post post2;
    private Follow follow1;
    private Follow follow2;
    private MultipartFile mockImage1;
    private MultipartFile mockImage2;

    @BeforeEach // 테스트 실행 전에 실행
    void setUp(){
        user=User.builder()
                .id(1L)
                .nickname("sh")
                .username("test1")
                .email("11@naver.com")
                .password("111")
                .introduce("test")
                .profileImageurl("https://example.com/default-profile.png")
                .isPublic(true)
                .build();
        user2=User.builder()
                .id(2L)
                .nickname("shh")
                .username("test2")
                .email("22@naver.com")
                .password("222")
                .introduce("test2")
                .profileImageurl("https://example.com/default-profile2.png")
                .isPublic(true)
                .build();


        image1=PostImage.builder()
                .id(1L)
                .postImageurl("/test1")
                .build();

        image2=PostImage.builder()
                .id(2L)
                .postImageurl("/test2")
                .build();

        image3=PostImage.builder()
                .id(3L)
                .postImageurl("/test3")
                .build();

        List<PostImage> images = List.of(image1, image2);

        post1=Post.builder()
                .id(1L)
                .content("테스트 게시글 1")
                .user(user)
                .images(images)
                .build();
        post2 = Post.builder()
                .id(2L)
                .content("테스트 게시글 2")
                .user(user) // 사전에 저장한 유저
                .likeNum(0)
                .images(new ArrayList<>())
                .build();

        // 팔로우 관계 초기화
        follow1 = Follow.builder()
                .id(1L)
                .following(user)
                .build();

        follow2 = Follow.builder()
                .id(2L)
                .following(user2)
                .build();


        mockImage1 = new MockMultipartFile("image1", "image1.jpg", "image/jpeg", "dummy data 1".getBytes());
        mockImage2 = new MockMultipartFile("image2", "image2.jpg", "image/jpeg", "dummy data 2".getBytes());

    }

    @Test
    @Transactional
    void 게시글_생성_테스트(){

        //given
        List<MultipartFile> images1=List.of(mockImage1,mockImage2);

        PostRequestDto request1 = PostRequestDto.builder()
                .content("테스트 게시글 1")
                .images(images1)
                .build();

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(postImageService.changeToPostImage(anyList(), any(Post.class)))
                .willReturn(List.of(image1, image2));

        //when
        postService.createPost(request1,1L);

        //then
        //PostRespository.save()가 호출되는지 체크
        verify(postRepository).save(any(Post.class));

    }



    @Test
    @Transactional
    void 하나의_특정_게시글_조회_테스트(){

        //given
        Long postId=1L;


        given(postRepository.findById(postId)).willReturn(Optional.of(post1));

        //when
        Post post=postRepository.findById(post1.getId()).orElseThrow(()-> new IllegalArgumentException("게시글 없음"));

        //then
        // 게시글 내용 확인
        assertEquals("테스트 게시글 1", post.getContent());
        assertEquals("/test1", post.getImages().get(0).getPostImageurl());
        assertEquals(0, post.getLikeNum());

    }

    @Test
    @Transactional
    void 팔로잉유저게시글들_조회_테스트(){

        Long userId=3L;//given


        given(followRepository.findFollowingsByFollowerId(userId)).willReturn(List.of(follow1, follow2));

        given(postRepository.findPostsByUserIdsIn(List.of(1L, 2L))).willReturn(List.of(post1, post2));

        //when
        List<PostResponseDto> response=postService.getAllPostsByFollowing(userId);

        //then
        assertEquals(2, response.size()); // 게시글 개수 검증
        assertEquals("테스트 게시글 1", response.get(0).getContent()); // 첫 번째 게시글 내용 확인
        assertEquals("테스트 게시글 2", response.get(1).getContent()); // 두 번째 게시글 내용 확인

        // followRepository, postRepository 호출 검증
        verify(followRepository).findFollowingsByFollowerId(userId);
        verify(postRepository).findPostsByUserIdsIn(List.of(1L, 2L));
    }

    @Test
    @Transactional
    void 게시글_수정_테스트(){

        //given
        Long userId=1L;
        Long postId=2L;
        PostRequestDto request= PostRequestDto.builder()
                .content("수정 게시글 2")
                .images(new ArrayList<>())
                .build();

        given(postRepository.findById(postId)).willReturn(Optional.of(post2));

        given(postImageService.changeToPostImage(anyList(), eq(post2)))
                .willReturn(new ArrayList<>());

        //when
        PostResponseDto response=postService.updatePost(postId, userId, request);

        //then
        assertEquals("수정 게시글 2", response.getContent()); // 게시글 내용이 수정되었는지 확인
        assertEquals(0, response.getImageUrlList().size()); // 이미지 개수 확인 (수정 후 이미지 리스트가 비었는지 확인)

        // 원본 게시글도 수정된 상태인지 검증
        verify(postRepository).findById(postId); // 게시글 조회 확인
        verify(postRepository).save(post2); // 수정된 게시글이 저장되었는지 확인
        verify(postImageService).changeToPostImage(anyList(), eq(post2)); // 이미지 변환이 호출되었는지 검증
    }

    @Test
    @Transactional
    void 게시글_삭제_테스트(){

        // given
        Long postId = 1L;

        given(postRepository.findById(postId)).willReturn(Optional.of(post1));

        // when
        postService.deletePost(postId);

        // then
        // 각 레포지토리와 서비스 메서드가 호출되었는지 검증
        verify(commentRepository).deleteByPostId(postId); // 댓글 삭제 검증
        verify(postLikeRepository).deleteByPostId(postId); // 좋아요 삭제 검증
        verify(postImageService).deleteAllImages(postId); // 이미지 삭제 검증
        verify(postRepository).delete(post1); // 게시글 삭제 검증
    }


}
