package com.example.dr_aids.patient.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PatientListResponseDto {

    private Long id; // 환자 ID
    private String name; // 환자 이름
    private String gender; // 환자 성별
    private Long age; // 환자 나이
    private LocalDate birth;
    private Boolean visiting; // 방문 여부
}
