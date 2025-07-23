package com.example.dr_aids.bloodpressure.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BloodPressureDto {
    private String time;
    private Long sbp;
    private Long dbp;
}

