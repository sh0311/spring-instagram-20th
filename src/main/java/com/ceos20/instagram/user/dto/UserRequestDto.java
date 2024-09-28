package com.ceos20.instagram.user.dto;

import com.ceos20.instagram.user.domain.UserStatus;
import lombok.Getter;

//회원가입 & 정보수정용
@Getter
public class UserRequestDto {  //정보수정할 때 바뀌지 않은 정보는 기존 user의 데이터 가져와서 채워줘야함
    private String nickname;
    private String username;
    private String email;
    private String introduce;
    private String profileImageurl;
    private UserStatus status;
}
