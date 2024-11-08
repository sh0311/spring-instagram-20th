package com.ceos20.instagram.reissue.service;

import com.ceos20.instagram.global.exception.BadRequestException;
import com.ceos20.instagram.global.exception.ExceptionCode;
import com.ceos20.instagram.global.exception.NotFoundException;
import com.ceos20.instagram.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JWTUtil jwtUtil;

    public String reissue(String refreshToken) {

        checkNull(refreshToken);
        checkExpired(refreshToken);
        checkIsRefresh(refreshToken);

        String username=jwtUtil.getUsername(refreshToken);
        Long userId=jwtUtil.getUserId(refreshToken);
        String role=jwtUtil.getRole(refreshToken);

        //새로운 access 토큰 생성
        String newAccess =jwtUtil.createJwt("access", username, role, userId, 30*60*1000L);

        return newAccess;
    }
    private void checkNull(String refreshToken) {
        if(refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException(ExceptionCode.NO_REFRESH_TOKEN);
        }
    }
    private void checkExpired(String refreshToken) {
        try{
            jwtUtil.isExpired(refreshToken);
        } catch(ExpiredJwtException e) {
            throw new BadRequestException(ExceptionCode.EXPIRED_REFRESH_TOKEN);
        }
    }
    private void checkIsRefresh(String refreshToken) {
        String category=jwtUtil.getCategory(refreshToken);
        if(!category.equals("refresh"))
            throw new BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN);
    }
}
