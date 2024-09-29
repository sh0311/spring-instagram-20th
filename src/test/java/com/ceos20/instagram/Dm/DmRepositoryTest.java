package com.ceos20.instagram.Dm;

import com.ceos20.instagram.comment.domain.Comment;
import com.ceos20.instagram.comment.repository.CommentRepository;
import com.ceos20.instagram.dm.domain.DmRoom;
import com.ceos20.instagram.dm.repository.DmRoomRepository;
import com.ceos20.instagram.dm.repository.MessageRepository;
import com.ceos20.instagram.post.domain.Post;
import com.ceos20.instagram.post.repository.PostRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DmRepositoryTest {
    @Autowired
    private DmRoomRepository dmRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;


    private User user;
    private User user2;
    private User user3;
    private DmRoom room;
    private DmRoom room2;

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
                .nickname("sh2")
                .username("test2")
                .email("11@naver.com")
                .password("111")
                .introduce("test")
                .profileImageurl("https://example.com/default-profile.png")
                .isPublic(true)
                .build();

        user3=User.builder()
                .nickname("sh3")
                .username("test3")
                .email("11@naver.com")
                .password("111")
                .introduce("test")
                .profileImageurl("https://example.com/default-profile.png")
                .isPublic(true)
                .build();

        room=DmRoom.builder()
                .user1(user)
                .user2(user2)
                .build();

        room2=DmRoom.builder()
                .user1(user3)
                .user2(user)
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        dmRoomRepository.save(room);
        dmRoomRepository.save(room2);

    }

    @Test
    @Transactional
    void 내_채팅방_조회(){

        //given
        Long userId=user.getId();

        //when
        List<DmRoom> rooms=dmRoomRepository.findRoomsByUserIdOrderByUpdatedAtDesc(userId);

        //then
        assertEquals(2, rooms.size());

    }


}

