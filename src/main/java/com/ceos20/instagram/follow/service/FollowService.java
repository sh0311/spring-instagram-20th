package com.ceos20.instagram.follow.service;

import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.follow.dto.FollowRequestDto;
import com.ceos20.instagram.follow.repository.FollowRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;

import com.ceos20.instagram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    //팔로우 신청하기
    @Transactional
    public void createFollow(FollowRequestDto followRequestDto){
        User sender=userService.findUserById(followRequestDto.getSenderId());
        User receiver=userService.findUserById(followRequestDto.getReceiverId());

        Follow newFollow=Follow.builder()
                .following(sender)
                .follower(receiver)
                .build(); //Builder.Default에 의해 isApproved는 false로 객체 생성됨

        followRepository.save(newFollow);
    }

    //팔로우 승인하기
    @Transactional
    public void approveFollow(FollowRequestDto followRequestDto){
        User sender=userService.findUserById(followRequestDto.getSenderId());
        User receiver=userService.findUserById(followRequestDto.getReceiverId());

        Follow target=followRepository.findFollowByFollowingIdAndFollowerId(followRequestDto.getSenderId(), followRequestDto.getReceiverId()).orElseThrow(()->new IllegalArgumentException("해당 팔로우 객체가 존재하지 않습니다."));

        target.approve();

        sender.increaseFollowingNum();
        receiver.increaseFollowerNum();

    }

    //팔로잉 취소하기
    @Transactional
    public void deleteFollowing(FollowRequestDto followRequestDto){
        Follow follow=followRepository.findFollowByFollowingIdAndFollowerId(followRequestDto.getSenderId(), followRequestDto.getReceiverId()).orElseThrow(()->new IllegalArgumentException("해당 팔로우 객체가 존재하지 않습니다."));

        // 팔로우 객체 삭제
        followRepository.delete(follow);

        // 팔로잉 및 팔로워 수 감소
        User sender = follow.getFollowing();
        User receiver = follow.getFollower();

        // 유저 정보를 수정 후 저장 (팔로잉/팔로워 수 갱신) -> 더티체킹
        sender.decreaseFollowingNum();
        receiver.decreaseFollowerNum();
        
    }

    public List<Follow> findFollowingsByFollowerId(Long userId){
        return followRepository.findFollowingsByFollowerId(userId);
    };

}
