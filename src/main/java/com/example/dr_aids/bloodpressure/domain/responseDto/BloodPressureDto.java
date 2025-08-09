package com.example.dr_aids.bloodpressure.domain.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BloodPressureDto {
    private String time;
    private Long sbp;
    private Long dbp;
}

