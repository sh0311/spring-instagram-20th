package com.ceos20.instagram.user.dto;

import com.ceos20.instagram.user.domain.User;
import com.ceos20.instagram.user.domain.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails { //UserDetailsService에서 찾은 user의 정보를 담는 객체 (dto같은 역할)

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection=new ArrayList<>();

        collection.add(new GrantedAuthority(){
            @Override
            public String getAuthority() {
                return user.getRole().name(); //enum->String으로 반환
            }
        });
        return collection;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public Long getId(){
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }

}
