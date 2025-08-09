package com.example.dr_aids.bloodpressure.domain.responseDto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BloodPressureRecentDto {
    private Long session; // 회차

    private List<BloodPressureDto> bloodPressureDto; // 혈압 DTO
}
