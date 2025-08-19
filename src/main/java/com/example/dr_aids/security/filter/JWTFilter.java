package com.example.dr_aids.security.filter;


import com.example.dr_aids.security.common.CustomUserDetails;
import com.example.dr_aids.security.common.JWTUtil;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final List<String> aiWhitelistPaths;

    // Swagger, 로그인, 회원가입, AI 경로는 JWT 인증 제외
    private final Set<String> excludePaths = Set.of(
            "/login",
            "/join",
            "/reissue",
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/webjars/",
            "/webjars/**",
            "/favicon.ico"
    );


    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository, List<String> aiWhitelistPaths) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.aiWhitelistPaths = aiWhitelistPaths; // AI 경로 화이트리스트
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return excludePaths.contains(path) || aiWhitelistPaths.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.substring(7); // "Bearer " 이후만 추출


        if (accessToken == null) {
            filterChain.doFilter(request, response);// 토큰이 없다면 다음 필터로 넘김
            return;
        }

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // username, role 값을 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        String email = jwtUtil.getEmail(accessToken);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        // 로그인된 사용자 정보, 비밀번호는 필요 없음 (JWT는 이미 인증된 상태), 권한 정보

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}
