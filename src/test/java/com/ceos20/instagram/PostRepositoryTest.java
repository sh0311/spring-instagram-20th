package com.ceos20.instagram;

import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.user.domain.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({PostRepository.class,UserRepository.class})
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private Post post1;
    private Post post2;
    private Post post3;
    private User user;

    @BeforeEach // 테스트 실행 전에 실행
    void setUp(){
        user=User.builder()
                .nickname("sh")
                .username("test1")
                .phone("010-1111-1111")
                .email("11@naver.com")
                .password("111")
                .introduce("test")
                .profile_image_url("https://example.com/default-profile.png")
                .isPublic(true)
                .build();

        post1=Post.builder()
                .content("testPost 1")
                .like_num(0)
                .user(user)
                .build();

        post2=Post.builder()
                .content("testPost 2")
                .like_num(1)
                .user(user)
                .build();

        post3=Post.builder()
                .content("testPost 3")
                .like_num(0)
                .user(user)
                .build();

        userRepository.save(user);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
    }

    @Test
    @Transactional
    void 게시글_조회_테스트(){

        //given
        Long userId=user.getId();

        //when
        List<Post> posts=postRepository.findByUser_Id(userId);

        //then
        // 게시글 갯수 확인
        assertEquals(3, posts.size());
        // 게시글 내용 확인
        assertEquals("testPost 1", posts.get(0).getContent());
        assertEquals("testPost 2", posts.get(1).getContent());
        assertEquals("testPost 3", posts.get(2).getContent());

    }
}
