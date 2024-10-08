package com.ceos20.instagram.user.dto;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.domain.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

//회원가입 & 정보수정용
@Getter
public class UserRequestDto {  //정보수정할 때 바뀌지 않은 정보는 기존 user의 데이터 가져와서 채워줘야함
    @NotBlank(message="닉네임은 필수 입력값입니다.")
    @Size(min=1, max=30, message="닉네임은 1-30글자입니다.")
    private String nickname;

    private String username;

    @Email(message="이메일 형식이어야합니다.")
    @NotBlank(message="이메일은 필수 입력값입니다.")
    private String email;

    private String password;
    private String introduce;
    private String profileImageurl;
    private UserStatus status;

    public User toEntity(){
        return User.builder()
                .nickname(nickname)
                .username(username)
                .email(email)
                .password(password)
                .introduce(introduce)
                .profileImageurl(profileImageurl)
                .status(status)
                .build();
    }
}
