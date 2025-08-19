package com.example.dr_aids.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AIRequestFilter extends OncePerRequestFilter {

    @Value("${ai.api.key}")
    private String aiApiKey;

    @Value("${ai.api.whitelist-paths}")
    private List<String> aiPathList;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (aiPathList.contains(path)) {
            String header = request.getHeader("Authorization");

            if (header == null || !header.equals("ApiKey " + aiApiKey)) {
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
        return !aiPathList.contains(path);
    }
}
