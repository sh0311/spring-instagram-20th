package com.ceos20.instagram.reissue.controller;

import com.ceos20.instagram.reissue.dto.RefreshResponseDto;
import com.ceos20.instagram.reissue.service.RefreshService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshController {

    private final RefreshService reissueService;

    //refresh 토큰 이용하여 access 토큰 재발급 (RefreshRotate를 위해 refresh 토큰도 함께 재발급) -> access토큰 없이 접근가능하도록 SecurityConfig에서 permitAll 해주기
    @PostMapping("/reissue")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken=getRefresh(request);

        RefreshResponseDto dto =reissueService.reissue(refreshToken);

        response.setHeader("access", dto.getAccessToken());
        response.addCookie(createCookie("refresh", dto.getRefreshToken()));

        return ResponseEntity.ok().build();
    }

    private String getRefresh(HttpServletRequest request) {

        String refresh=null;

        Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals("refresh")){
                refresh=cookie.getValue();
            }
        }
        return refresh;
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie=new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
