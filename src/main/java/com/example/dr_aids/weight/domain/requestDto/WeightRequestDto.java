package com.example.dr_aids.weight.domain.requestDto;

import lombok.Data;

@Data
public class WeightRequestDto {
    private Long patientId; // 환자 ID
    private Long session; // 세션 회차

    private Double preWeight; // 전 체중
    private Double postWeight; // 후 체중
    private Double dryWeight; // 건조 체중
    private Double targetUF; // 목표 UF
    private Double controlUF; // 조절 UF (예: ">1kg 초과")
}
