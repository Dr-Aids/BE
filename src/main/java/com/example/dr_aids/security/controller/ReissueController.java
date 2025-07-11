package com.example.dr_aids.security.controller;


import com.example.dr_aids.security.common.JWTUtil;
import com.example.dr_aids.security.domain.RefreshToken;
import com.example.dr_aids.security.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@Tag(name = "Token Reissue", description = "토큰 재발급 API")
@RestController
@RequestMapping("/reissue")
@Slf4j
public class ReissueController {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    private final long ACCESS_TOKEN_EXP = 7200000L; // 2시간
    private final long REFRESH_TOKEN_EXP = 604800000L; // 7일

    public ReissueController(JWTUtil jwtUtil, RefreshTokenRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshRepository;
    }

    @Operation(
            summary = "Access/Refresh 토큰 재발급",
            description = "Refresh 토큰을 쿠키로 받아 새로운 Access 토큰과 Refresh 토큰을 발급합니다.\n\n" +
                    "※ 기존 Refresh 토큰은 폐기되며 새 Refresh 토큰은 쿠키로 다시 저장됩니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(
                            example = "{\"code\": \"S001\", \"message\": \"New tokens issued\"}"
                    ))
            ),
            @ApiResponse(responseCode = "400", description = "쿠키에 Refresh 토큰 없음",
                    content = @Content(schema = @Schema(
                            example = "{\"code\": \"E001\", \"error\": \"Refresh token not found in cookies\"}"
                    ))
            ),
            @ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 토큰",
                    content = @Content(schema = @Schema(
                            example = "{\"code\": \"E002\", \"error\": \"Refresh token expired\"}"
                    ))
            ),
            @ApiResponse(responseCode = "401", description = "토큰이 Refresh 토큰이 아님",
                    content = @Content(schema = @Schema(
                            example = "{\"code\": \"E004\", \"error\": \"Token is not a refresh token\"}"
                    ))
            ),
            @ApiResponse(responseCode = "401", description = "DB에 Refresh 토큰이 존재하지 않음",
                    content = @Content(schema = @Schema(
                            example = "{\"code\": \"E005\", \"error\": \"Refresh token not found in DB\"}"
                    ))
            )
    })
    @PostMapping
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 1. 쿠키에서 refresh token 추출
        String refresh = extractRefreshTokenFromCookie(request);
        if (refresh == null) {
            return buildErrorResponse("E001", "Refresh token not found in cookies", HttpStatus.BAD_REQUEST);
        }

        // 2. 토큰 유효성 검사
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            log.warn("Refresh token expired: {}", refresh);
            return buildErrorResponse("E002", "Refresh token expired", HttpStatus.UNAUTHORIZED);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid refresh token format: {}", e.getMessage());
            return buildErrorResponse("E003", "Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        // 3. 토큰 유형 확인 (access or refresh)
        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            return buildErrorResponse("E004", "Token is not a refresh token", HttpStatus.UNAUTHORIZED);
        }

        // 4. DB에서 refresh 토큰 존재 확인
        if (!refreshTokenRepository.existsByRefresh(refresh)) {
            return buildErrorResponse("E005", "Refresh token not found in DB", HttpStatus.UNAUTHORIZED);
        }

        // 5. 사용자 정보 추출
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);
        String email = jwtUtil.getEmail(refresh);

        // 6. 새 토큰 생성
        String newAccess = jwtUtil.createJwt("access", username, role, email, ACCESS_TOKEN_EXP);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, email, REFRESH_TOKEN_EXP);

        // 7. 기존 refresh 삭제 후 새 refresh 저장
        refreshTokenRepository.deleteByRefresh(refresh);
        saveNewRefreshToken(username, newRefresh, REFRESH_TOKEN_EXP);

        // 8. 새 토큰 전송
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        log.info("New tokens issued for user '{}'", username);
        return ResponseEntity.ok(Map.of(
                "code", "S001",
                "message", "New tokens issued"
        ));
    }

    /**
     * 쿠키에서 refresh 토큰 추출
     */
    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 새 Refresh 토큰 저장
     */
    private void saveNewRefreshToken(String username, String refresh, long expireMs) {
        Date expiration = new Date(System.currentTimeMillis() + expireMs);
        RefreshToken entity = new RefreshToken();
        entity.setUsername(username);
        entity.setRefresh(refresh);
        entity.setExpiration(expiration);
        refreshTokenRepository.save(entity);
    }

    /**
     * Refresh 토큰을 담는 HttpOnly 쿠키 생성
     */
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge((int) (REFRESH_TOKEN_EXP / 1000));
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * 에러 응답 생성기
     */
    private ResponseEntity<Map<String, String>> buildErrorResponse(String code, String message, HttpStatus status) {
        return ResponseEntity.status(status).body(Map.of(
                "code", code,
                "error", message
        ));
    }
}