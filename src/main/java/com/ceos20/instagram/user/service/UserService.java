package com.ceos20.instagram.user.service;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.UserRequestDto;
import com.ceos20.instagram.user.dto.UserResponseDto;
import com.ceos20.instagram.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
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
}
