package com.ceos20.instagram.Comment;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
                .email("11@naver.com")
                .password("111")
                .introduce("test")
                .profileImageurl("https://example.com/default-profile.png")
                .isPublic(true)
                .build();

        post1=Post.builder()
                .content("testPost 1")
                .likeNum(0)
                .user(user)
                .build();

        parent=Comment.builder()
                .content("I'm parent")
                .post(post1)
                .user(user)
                .build();

        child1=Comment.builder()
                .content("I'm child1")
                .post(post1)
                .user(user)
                .parent(parent)
                .build();

        child2=Comment.builder()
                .content("I'm child2")
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
        List<Comment> parents=commentRepository.findParentsByPostId(postId);

        //then
        // 게시글 갯수 확인
        assertEquals(1, parents.size());
        //assertEquals(2, childs.size());

        // 게시글 내용 확인
        assertEquals("I'm parent", parents.get(0).getContent());
        //assertEquals("I'm child1", childs.get(0).getContent());
        //assertEquals("I'm child2", childs.get(1).getContent());

    }


}
