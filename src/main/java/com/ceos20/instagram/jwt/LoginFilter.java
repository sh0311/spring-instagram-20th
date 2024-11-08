package com.ceos20.instagram.jwt;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            //json 문자열로부터 객체를 만들어낸다
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getReader(), User.class); //user에 object를 담아줌
            System.out.println(user);

            //클라이언트 요청에서 username, password 추출
            String email = user.getEmail();
            String password = user.getPassword();

            System.out.println("유저 이메일: " + email);
            //스프링 시큐리티에서 email과 password를 검증하기 위해서는 token에 담아야 함
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

            //SecurityContextHolder.getContext().setAuthentication(authToken);

            //검증하기 위해 email, password를 토큰에 담아 Authetnication에 넘김
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override //로그인 성공시 거치게 된다 (토큰 생성해줌)
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //인증이 완료된 사용자 객체 가져옴
        CustomUserDetails customUserDetails= (CustomUserDetails) authentication.getPrincipal();

        //토큰 생성에 필요한 사용자 정보들 추출
        String username=customUserDetails.getUsername();

        Long userId=customUserDetails.getId();

        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator=authorities.iterator();
        GrantedAuthority auth=iterator.next(); //첫번째 권한만 가져와 저장

        String role=auth.getAuthority();

        //토큰 생성 (access, refresh)
        String access=jwtUtil.createJwt("access", username, role, userId, 30*60*1000L); //30분
        String refresh=jwtUtil.createJwt("refresh", username, role, userId, 24*60*60*1000L); //24시간

        //Authorization 헤더를 통해 토큰을 전송
        response.addHeader("access",access);
        response.addCookie(createCookie("refresh",refresh));
        response.setStatus(HttpStatus.OK.value());
    }
    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);   //쿠키의 생명주기 (24시간)
        //cookie.setSecure(true);   //Https 통신할 때 넣어주기
        //cookie.setPath("/");   //쿠키가 웹 애플리케이션의 모든 경로에서 유효하도록 설정
        cookie.setHttpOnly(true);   //클라이언트 단에서 자바스크립트로 쿠키에 접근하지 못하도록 막아주기

        return cookie;
    }

    @Override //로그인 실패시 거치게 된다
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }
}



