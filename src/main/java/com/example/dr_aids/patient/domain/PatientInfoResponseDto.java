package com.example.dr_aids.patient.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PatientInfoResponseDto {
    private Long id;
    private String name;
    private Long age;
    private LocalDate birth; // ex)2003-11-18
    private String gender;
    private String disease;
    private String PIC; // Person In Charge : 담당의사
}
