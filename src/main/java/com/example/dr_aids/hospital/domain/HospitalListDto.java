package com.example.dr_aids.hospital.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HospitalListDto {
    private Long id; // 병원 ID
    private String hospitalName; // 병원 이름
}
