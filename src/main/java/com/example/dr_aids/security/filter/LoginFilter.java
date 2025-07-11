package com.example.dr_aids.security.filter;

import com.example.dr_aids.security.common.CustomUserDetails;
import com.example.dr_aids.security.common.JWTUtil;
import com.example.dr_aids.user.domain.LoginDTO;
import com.example.dr_aids.security.domain.RefreshToken;
import com.example.dr_aids.security.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    //authenticationManager의 역할 : 로그인 정보 추출, 인증 토큰 생성
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshTokenRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        LoginDTO loginDTO;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginDTO = objectMapper.readValue(messageBody, LoginDTO.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인 요청의 형식을 읽을 수 없습니다.", e);
        }

        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        log.info(email + "의 로그인 시도");

        // 토큰은 authenticationManager이 username, password를 검증하기 위해서 발급하는 것
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        //검증이 잘 되면 Authentication 반환, 안되면 exception 반환
        return authenticationManager.authenticate(authToken);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String email = customUserDetails.getEmail();
        String access = jwtUtil.createJwt("access", username, role, email, 7200000L); // 2 hours in milliseconds
        String refresh = jwtUtil.createJwt("refresh", username, role, email, 604800000L); // 7 days in milliseconds

        addRefreshEntity(username, refresh, 604800000L); // 7 days in milliseconds

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
        log.info("로그인 성공, access token 발급 완료");
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshEntity = new RefreshToken();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date);

        refreshTokenRepository.save(refreshEntity);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true); //쿠키 암호화 전송 https://howisitgo1ng.tistory.com/entry/HTTP-Only%EC%99%80-Secure-Cookie
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
