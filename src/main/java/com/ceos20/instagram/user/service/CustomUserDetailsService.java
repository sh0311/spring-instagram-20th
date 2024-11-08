package com.ceos20.instagram.user.service;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.dto.CustomUserDetails;
import com.ceos20.instagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    public final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //로그인 요청이 들어온 email을 바탕으로 유저를 찾아 제대로 된 유저인지 검증
        User userData=userRepository.findByEmail(email);

        if(userData!=null){
            return new CustomUserDetails(userData); //UserDetails에 user정보 담아서 return하면 AuthenticationManager가 이를 이용해 검증한다.
        }
        return null;
    }


}
