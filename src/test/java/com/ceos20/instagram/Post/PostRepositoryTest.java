package com.ceos20.instagram.Post;

import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.post.repository.PostImageRepository;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import jakarta.transaction.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    private User user;
    private User user2;
    private Post post1;
    private Post post2;
    private PostImage image1;
    private PostImage image2;
    private PostImage image3;
    private Follow follow1;
    private Follow follow2;

    @BeforeEach
        // 테스트 실행 전에 실행
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

        userRepository.save(user);
        userRepository.save(user2);



        post1=Post.builder()
                .content("테스트 게시글 1")
                .user(user)
                .build();
        post2 = Post.builder()
                .content("테스트 게시글 2")
                .user(user)
                .build();

        image1=PostImage.builder()
                .post(post1)
                .postImageurl("/test1")
                .build();

        image2=PostImage.builder()
                .post(post1)
                .postImageurl("/test2")
                .build();

        image3=PostImage.builder()
                .post(post2)
                .postImageurl("/test3")
                .build();

        List<PostImage> images = List.of(image1, image2);
        List<PostImage> images2 = List.of(image3);

        post1.mapImages(images);
        post2.mapImages(images2);

        // 팔로우 관계 초기화
        follow1 = Follow.builder()
                .following(user)
                .build();

        follow2 = Follow.builder()
                .following(user2)
                .build();


        postRepository.save(post1);
        postRepository.save(post2);
    }


    @Test
    @Transactional
    void 게시글_생성() {
        // given

        image1=PostImage.builder()
                .postImageurl("/test1")
                .build();

        image2=PostImage.builder()
                .postImageurl("/test2")
                .build();
        List<PostImage> images = List.of(image1, image2);
        post1=Post.builder()
                .content("테스트 게시글 1")
                .user(user)
                .images(images)
                .build();

        //when
        Post post = postRepository.save(post1);

        // then
        assertEquals(0,post.getLikeNum());
        assertEquals(new ArrayList<>(),post.getImages());
    }



    @Test
    @Transactional
    void 게시글_이미지_포함한_단일_게시글_조회() {
        // given & when
        Post post1 = postRepository.findById(this.post1.getId()).get();

        // then
        assertThat(post1).isEqualTo(this.post1);
    }


    @Test
    @Transactional
    void 팔로우_유저의_게시글_리스트_조회() {
        // given
        List<Long> followingIds = List.of(user.getId(), user2.getId());

        // when
        List<Post> posts = postRepository.findPostsByUserIdsIn(followingIds);

        // then
        assertEquals(2, posts.size());
    }


    @Test
    @Transactional
    void 특정_유저의_게시글_리스트_조회() {
        // given
        Long userId = 1L;

        // when
        List<Post> posts = postRepository.findPostWithImageByUserId(userId);

        // then
        assertEquals(2, posts.size());
        assertEquals("테스트 게시글 1", posts.get(0).getContent());
    }


}
