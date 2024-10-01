package com.ceos20.instagram.Follow;


import com.ceos20.instagram.dm.domain.DmRoom;
import com.ceos20.instagram.dm.repository.DmRoomRepository;
import com.ceos20.instagram.dm.repository.MessageRepository;
import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.follow.repository.FollowRepository;
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
public class FollowRepositoryTest {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;



    private User user;
    private User user2;
    private User user3;
    private Follow follow1;
    private Follow follow2;

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

        follow1=Follow.builder()
                    .following(user2)
                    .follower(user)
                    .build();

        follow2=Follow.builder()
                .following(user3)
                .follower(user)
                .build();



        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);
        followRepository.save(follow1);
        followRepository.save(follow2);

    }


    @Test
    @Transactional
    void 내_팔로잉목록_조회(){

        //given
        Long userId=user.getId();

        //when
        List<Follow> followingList=followRepository.findFollowingsByFollowerId(userId);

        //then
        assertEquals(2, followingList.size());

    }
}
