package com.ceos20.instagram.jwt;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.domain.UserRole;
import com.ceos20.instagram.user.dto.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

//JWT를 검증하는 필터
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken=request.getHeader("access");

        //access토큰이 없다면 다음 필터로 넘김
        if(accessToken==null){
            filterChain.doFilter(request, response);

            return;
        }

        try{
            jwtUtil.isExpired(accessToken);
        }catch (ExpiredJwtException e){

            //response body
            PrintWriter writer=response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }

        //토큰이 access토큰인지 확인 (토큰 발급 시 payload에 명시해둠)
        String category=jwtUtil.getCategory(accessToken);
        if(!category.equals("access")){

            //response body
            PrintWriter writer=response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        }

        String username=jwtUtil.getUsername(accessToken);
        String roleStr=jwtUtil.getRole(accessToken);
        UserRole roleEnum = UserRole.valueOf(roleStr); // 문자열을 Role enum으로 변환
        Long userId=jwtUtil.getUserId(accessToken);

        User user= User.builder()
                .username(username)
                .role(roleEnum)
                .id(userId)
                .build();


        //UserDetails에 유저 정보 객체 담기
        CustomUserDetails customUserDetails=new CustomUserDetails(user);

        //인증 성공 후 만든 Authentication(UsernamePasswordAuthenticationToken) -> 따라서 비밀번호는 삭제해야 하므로 null로 Authentication 객체 생성
        Authentication authToken=new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
