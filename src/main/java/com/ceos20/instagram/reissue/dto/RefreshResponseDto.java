package com.ceos20.instagram.reissue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshResponseDto {
    private String accessToken;
    private String refreshToken;

}
