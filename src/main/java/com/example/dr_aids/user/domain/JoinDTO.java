package com.example.dr_aids.user.domain;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinDTO {
    private String username;
    private String password;
    private String email;
    private String role; // 입력 예시 : DOCTOR, NURSE
    private String hospitalname; // 병원 이름
}
