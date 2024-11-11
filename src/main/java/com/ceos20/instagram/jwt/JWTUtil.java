package com.ceos20.instagram.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret){
        this.secretKey=new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    //토큰에서 내부정보 꺼내는 4가지 (username, role, expired, user_id)
    //토큰이 우리 서버에서 생성된 게 맞는지(우리가 가지고 있는 키와 맞는지) 확인 후 builder타입으로 return. claim확인, payload에서 특정한 데이터 가져옴
    public String getUsername(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //user id 얻기 위해 추가
    public Long getUserId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("user_id", Long.class);
    }

    //category (access인지 refresh인지 식별하기 위해)
    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }



    //토큰 생성 (user id 빼고 나중에 인증된 customUserDetails 만들때 username으로 user 객체 찾고 얘의 id 가져와 custom객체에 저장해도 @AuthenticationPrincipal에서 getId() 가능)
    public String createJwt(String category, String username, String role, Long userId, Long expiredMs){
        return Jwts.builder()
                .claim("category", category)
                .claim("user_id", userId)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))  //토큰의 발행시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs))  //이 토큰이 언제 소멸되는지
                .signWith(secretKey)  //토큰을 secretkey를 이용해 암호화
                .compact();

    }
}
