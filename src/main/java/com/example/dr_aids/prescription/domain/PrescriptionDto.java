package com.example.dr_aids.prescription.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PrescriptionDto {
    private Long patientId;
    private String date;
    private String hematapoieticAgent; //조혈제
    private Long IU;
    private String description;

}
