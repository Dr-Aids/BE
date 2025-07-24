package com.example.dr_aids.user.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponseDto {
    private Long id; // 사용자 ID
    private String username; // 사용자 이름
    private String role; // 사용자 역할 (예: "ROLE_USER", "ROLE_ADMIN")
    private String email; // 이메일 주소
    private String hospitalName; // 병원 이름 (선택적, null일 수 있음)

}
