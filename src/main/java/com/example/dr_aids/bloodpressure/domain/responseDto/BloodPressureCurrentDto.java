package com.example.dr_aids.bloodpressure.domain.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BloodPressureCurrentDto {
    private Long startSbp; // 시작 수축기 혈압
    private Long startDbp; // 시작 이완기 혈압
    private Long lastSbp; // 마지막 수축기 혈압
    private Long lastDbp; // 마지막 이완기 혈압
}
