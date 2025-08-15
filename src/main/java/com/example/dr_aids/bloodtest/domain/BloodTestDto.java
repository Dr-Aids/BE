package com.example.dr_aids.bloodtest.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BloodTestDto {
    private Long patientId;
    private Double iron;
    private Double ferritine;
    private Double TIBC;
    private Double PTH;
    private Double hemoglobin;
    private Double hematocrit;
    private String date;
}
