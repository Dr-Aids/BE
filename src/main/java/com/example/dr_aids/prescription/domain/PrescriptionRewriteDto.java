package com.example.dr_aids.prescription.domain;

import lombok.Data;

@Data
public class PrescriptionRewriteDto {
    private Long id; // 처방 ID
    private Long patientId;
    private String date;
    private String hematapoieticAgent; //조혈제
    private Long IU;
    private String description;
}
