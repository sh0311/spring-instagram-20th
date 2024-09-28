package com.ceos20.instagram.follow.service;

import com.ceos20.instagram.follow.domain.Follow;
import com.ceos20.instagram.follow.dto.FollowRequestDto;
import com.ceos20.instagram.follow.repository.FollowRepository;
import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    //팔로우 신청하기
    public void createFollow(FollowRequestDto followRequestDto){
        User sender=userRepository.findById(followRequestDto.getSenderId()).orElseThrow(()-> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));
        User receiver=userRepository.findById(followRequestDto.getReceiverId()).orElseThrow(()-> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));

        Follow newFollow=Follow.builder()
                .following(sender)
                .follower(receiver)
                .build(); //Builder.Default에 의해 isApproved는 false로 객체 생성됨

        followRepository.save(newFollow);
    }

    //팔로우 승인하기
    public void approveFollow(FollowRequestDto followRequestDto){
        User sender=userRepository.findById(followRequestDto.getSenderId()).orElseThrow(()-> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));
        User receiver=userRepository.findById(followRequestDto.getReceiverId()).orElseThrow(()-> new IllegalArgumentException("해당 id의 유저가 존재하지 않습니다."));

        Follow target=followRepository.findFollowByFollowingIdAndFollowerId(followRequestDto.getSenderId(), followRequestDto.getReceiverId()).orElseThrow(()->new IllegalArgumentException("해당 팔로우 객체가 존재하지 않습니다."));

        target.approve();

        sender.increaseFollowingNum();
        receiver.increaseFollowerNum();

        followRepository.save(target);
    }

    //팔로잉 취소하기
    public void deleteFollowing(FollowRequestDto followRequestDto){
        Follow follow=followRepository.findFollowByFollowingIdAndFollowerId(followRequestDto.getSenderId(), followRequestDto.getReceiverId()).orElseThrow(()->new IllegalArgumentException("해당 팔로우 객체가 존재하지 않습니다."));

        // 팔로우 객체 삭제
        followRepository.delete(follow);

        // 팔로잉 및 팔로워 수 감소
        User sender = follow.getFollowing();
        User receiver = follow.getFollower();

        sender.decreaseFollowingNum();
        receiver.decreaseFollowerNum();

        // 유저 정보를 저장 (팔로잉/팔로워 수 갱신)
        userRepository.save(sender);
        userRepository.save(receiver);
    }


}
