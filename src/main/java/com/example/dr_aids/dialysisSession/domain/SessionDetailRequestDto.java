package com.example.dr_aids.dialysisSession.domain;

import lombok.Data;

@Data
public class SessionDetailRequestDto {
    private Long patientId; // 환자 ID
    private Long session; // 세션 회차
}
