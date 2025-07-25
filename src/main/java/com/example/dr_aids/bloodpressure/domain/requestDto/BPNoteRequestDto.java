package com.example.dr_aids.bloodpressure.domain.requestDto;

import lombok.Data;

@Data
public class BPNoteRequestDto {
    private Long patientId; // 사용자 ID
    private Long session; // 회차

    private Long bloodId; // 혈압 기록의 ID
    private String note;
    private Boolean isChecked; // 체크 여부
}
