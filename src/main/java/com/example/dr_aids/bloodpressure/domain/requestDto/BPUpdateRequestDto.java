package com.example.dr_aids.bloodpressure.domain.requestDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BPUpdateRequestDto {
    private Long patientId; // 사용자 ID
    private Long session; // 회차
    private Long bloodId; // 혈압 기록의 ID
    private Long sbp; // 수축기 혈압
    private Long dbp; // 이완기 혈압
    private LocalDateTime measurementTime; // 측정 시간
}
