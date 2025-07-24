package com.example.dr_aids.dialysisSession.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SessionSaveRequestDto {
    private Long patientId; // 환자 ID

    private Double preWeight; // 전 체중
    private Double dryWeight; // 건조 체중
    private Double targetUF; // 목표 UF
    private LocalDate date; // 세션 날짜
}
