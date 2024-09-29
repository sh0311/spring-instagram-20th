package com.ceos20.instagram.PostLike;

import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.domain.PostImage;
import com.ceos20.instagram.post.domain.PostLike;
import com.ceos20.instagram.post.repository.PostLikeRepository;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostLikeRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostLikeRepository postLikeRepository;

    private User user;
    private User user2;
    private Post post1;
    private Post post2;


    @BeforeEach
        // 테스트 실행 전에 실행
    void setUp(){
        user=User.builder()
                .nickname("sh")
                .username("test1")
                .email("11@naver.com")
                .password("111")
                .introduce("test")
                .profileImageurl("https://example.com/default-profile.png")
                .isPublic(true)
                .build();
        user2=User.builder()
                .nickname("shh")
                .username("test2")
                .email("22@naver.com")
                .password("222")
                .introduce("test2")
                .profileImageurl("https://example.com/default-profile2.png")
                .isPublic(true)
                .build();


        post1 = Post.builder()
                .content("테스트 게시글 1")
                .user(user) // 사전에 저장한 유저
                .likeNum(0)
                .images(new ArrayList<>())
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        postRepository.save(post1);
    }



    @Test
    @Transactional
    void 좋아요_조회_테스트() {
        // given
        PostLike like= PostLike.builder()
                .post(post1)
                .user(user)
                .build();
        PostLike like2= PostLike.builder()
                .post(post1)
                .user(user2)
                .build();

        //when
        postLikeRepository.save(like);
        postLikeRepository.save(like2);
        List<PostLike> likes = postLikeRepository.findByPostId(post1.getId());

        // then
        assertThat(likes.size()).isEqualTo(2);
    }



}
