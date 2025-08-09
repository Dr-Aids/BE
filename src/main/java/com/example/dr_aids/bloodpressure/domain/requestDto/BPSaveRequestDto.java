package com.example.dr_aids.bloodpressure.domain.requestDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BPSaveRequestDto {
    private Long patientId; // 사용자 ID
    private Long session; // 회차
    private Long sbp;
    private Long dbp;
    //"yyyy-MM-dd'T'HH:mm:ss"
    private LocalDateTime measurementTime;  // ✅ 자동 변환됨 (Jackson이 처리함)
}
