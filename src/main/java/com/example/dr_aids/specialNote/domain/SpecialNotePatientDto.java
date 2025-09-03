package com.example.dr_aids.specialNote.domain;

import com.example.dr_aids.bloodpressure.domain.responseDto.BloodPressureDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SpecialNotePatientDto {
    private Long session;
    private String date;

    private Long sbp;
    private Long dbp;
    private Double preWeight;

    private List<SpecialNoteDto> specialNotes; // 특이사항 DTO

}