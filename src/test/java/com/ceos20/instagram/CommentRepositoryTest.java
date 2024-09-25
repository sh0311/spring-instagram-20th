package com.ceos20.instagram;

import com.ceos20.instagram.comment.domain.Comment;
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
@Import({CommentRepository.class, UserRepository.class,PostRepository.class})
public class CommentRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private Post post1;
    private User user;
    private Comment parent;
    private Comment child1;
    private Comment child2;

    @BeforeEach
        // 테스트 실행 전에 실행
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

        parent=Comment.builder()
                .context("I'm parent")
                .post(post1)
                .user(user)
                .build();

        child1=Comment.builder()
                .context("I'm child1")
                .post(post1)
                .user(user)
                .parent(parent)
                .build();

        child2=Comment.builder()
                .context("I'm child2")
                .post(post1)
                .user(user)
                .parent(parent)
                .build();





        userRepository.save(user);
        postRepository.save(post1);
        commentRepository.save(parent);
        commentRepository.save(child1);
        commentRepository.save(child2);

    }

    @Test
    @Transactional
    void 댓글_조회_테스트(){

        //given
        Long postId=post1.getId();
        Long parentId=parent.getId();

        //when
        List<Comment> parents=commentRepository.findByPost_Id(postId);
        List<Comment> childs=commentRepository.findByParent_Id(parentId);

        //then
        // 게시글 갯수 확인
        assertEquals(1, parents.size());
        assertEquals(2, childs.size());

        // 게시글 내용 확인
        assertEquals("I'm parent", parents.get(0).getContext());
        assertEquals("I'm child1", childs.get(0).getContext());
        assertEquals("I'm child2", childs.get(1).getContext());

    }
}
