package com.ceos20.instagram.user.dto;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.domain.UserStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String nickname;
    private String username;
    private String email;
    private String introduce;
    private String profileImageurl;
    private boolean isPublic;
    private UserStatus status;
    private int followerCount;
    private int followingCount;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .email(user.getEmail())
                .introduce(user.getIntroduce())
                .profileImageurl(user.getProfileImageurl())
                .isPublic(user.isPublic())
                .status(user.getStatus())
                .followerCount(user.getFollowerCount())
                .followingCount(user.getFollowingCount())
                .build();
    }
}
