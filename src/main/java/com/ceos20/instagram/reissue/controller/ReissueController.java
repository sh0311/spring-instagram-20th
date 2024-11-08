package com.ceos20.instagram.reissue.controller;

import com.ceos20.instagram.reissue.service.ReissueService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReissueController {

    private final ReissueService reissueService;

    //refresh 토큰 이용하여 access 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken=getRefresh(request);
        String newAccess=reissueService.reissue(refreshToken);
        response.setHeader("access", newAccess);
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


}
