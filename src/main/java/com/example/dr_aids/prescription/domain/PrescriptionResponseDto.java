package com.example.dr_aids.prescription.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrescriptionResponseDto {
    private Long id;
    private String date;
    private String hematapoieticAgent; //조혈제
    private Long IU;
    private String description;
}
