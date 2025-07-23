package com.example.dr_aids.patient.domain.responseDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PatientSessionInfoResponseDto {
    private Long session; //세션 회차
    private LocalDate date; //세션 날짜
}
