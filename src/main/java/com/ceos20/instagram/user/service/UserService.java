package com.ceos20.instagram.user.service;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.UserRequestDto;
import com.ceos20.instagram.user.dto.UserResponseDto;
import com.ceos20.instagram.user.repository.UserRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    // 회원가입


    // user 한명 조회
    public UserResponseDto getUser(Long userId){
        User user=userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("해당 id의 유저가 없습니다."));
        return UserResponseDto.of(user);
    }

    // user 정보 수정
    @Transactional
    public UserResponseDto updateUser(UserRequestDto userRequestDto){
        User user=userRepository.findByNickname(userRequestDto.getNickname()).orElseThrow(()->new IllegalArgumentException("해당 id의 유저가 없습니다."));
        user.update(userRequestDto);
        userRepository.save(user);
        return UserResponseDto.of(user);
    }

    // id로 user 존재 여부 확인 + user 반환
    public User findUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("해당 id의 유저를 찾을 수 없습니다."));
    }

    // user 저장
    public void saveUser(User user){
        userRepository.save(user);
    }
}
