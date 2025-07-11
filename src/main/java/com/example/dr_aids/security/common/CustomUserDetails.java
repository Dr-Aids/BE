package com.example.dr_aids.security.common;


import com.example.dr_aids.user.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails { // CustomUserDetails는 UserDetails 인터페이스를 구현하여 Spring Security에서 사용자 정보를 담는 클래스입니다.
    private final User user;
    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(user.getRole().getValue());
            }
        });
        return authorities;
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }
    @Override
    public String getUsername() {

        return user.getUsername();
    }
    public String getEmail() {

        return user.getEmail();
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

