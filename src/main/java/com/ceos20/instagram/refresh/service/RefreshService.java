package com.ceos20.instagram.refresh.service;

import com.ceos20.instagram.global.exception.BadRequestException;
import com.ceos20.instagram.global.exception.ExceptionCode;
import com.ceos20.instagram.jwt.JWTUtil;
import com.ceos20.instagram.refresh.domain.Refresh;
import com.ceos20.instagram.refresh.dto.RefreshResponseDto;
import com.ceos20.instagram.refresh.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Transactional
    public RefreshResponseDto reissue(String refreshToken) {

        checkNull(refreshToken);
        checkExpired(refreshToken);
        checkIsRefresh(refreshToken);

        String username=jwtUtil.getUsername(refreshToken);
        Long userId=jwtUtil.getUserId(refreshToken);
        String role=jwtUtil.getRole(refreshToken);

        //새로운 access, refresh 토큰 생성
        String newAccess =jwtUtil.createJwt("access", username, role, userId, 30*60*1000L);
        String newRefresh=jwtUtil.createJwt("refresh", username, role, userId, 24*60*60*1000L);

        //요청받은 refresh token이 db에 존재하지 않으면 에러 반환, 존재하면 기존의 refresh tokendms db에서 삭제하고 새로 만들어진 refreshToken 저장
        Refresh existingToken = refreshRepository.findByRefresh(refreshToken)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.INVALID_REFRESH_TOKEN));

        refreshRepository.delete(existingToken);


        saveRefreshToken(username, userId, newRefresh, 24*60*60*1000L);

        return new RefreshResponseDto(newAccess, newRefresh);
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
    private void saveRefreshToken(String username, Long userId, String newRefresh, Long expiredMs) {

        Date date=new Date(System.currentTimeMillis()+expiredMs);

        Refresh refresh=Refresh.builder()
                .username(username)
                .userId(userId)
                .refresh(newRefresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refresh);

    }
}
