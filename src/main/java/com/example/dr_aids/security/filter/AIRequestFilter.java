package com.example.dr_aids.security.filter;

import com.example.dr_aids.security.common.AIProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
@RequiredArgsConstructor
@Component
public class AIRequestFilter extends OncePerRequestFilter {

    private final AIProperties aiProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (aiProperties.getWhitelistPaths().contains(path)) {
            String header = request.getHeader("Authorization");

            if (header == null || !header.equals("ApiKey " + aiProperties.getKey())) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
                return;
            }

            // JWT 인증은 건너뛰고 다음 필터로 넘어감
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // AI 경로가 아니면 필터 작동 안 함
        String path = request.getRequestURI();
        return !aiProperties.getWhitelistPaths().contains(path);
    }
}
