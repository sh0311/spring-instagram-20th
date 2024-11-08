package com.ceos20.instagram.global.config;


import com.ceos20.instagram.jwt.JWTFilter;
import com.ceos20.instagram.jwt.JWTUtil;
import com.ceos20.instagram.jwt.LoginFilter;
import com.ceos20.instagram.reissue.repository.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity //security를 위한 config라서 (모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션)
@RequiredArgsConstructor
public class SecurityConfig {

    //사용자의 비밀번호를 암호화 할 때 사용
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;

    //AuthenticationManager Bean 등록 (AuthenticationManager를 반환해준다)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth)-> auth.disable());

        http
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/login", "/", "/users").permitAll() //로그인, 회원가입은 토큰 없이 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN") //ADMIN 권한을 가진 사용자만 가능
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().authenticated()); //이외에는 로그인 한 사용자만 접근가능

        //JWTFilter 등록 (jwt토큰 검증)
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
        //LoginFilter 등록
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);


        //jwt에서는 세션을 stateless 상태로 관리해야 한다
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }




}
