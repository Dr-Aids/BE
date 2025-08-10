package com.example.dr_aids.bloodtest.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BloodTestOnlyHbResponseDto {
    private Long id; // 검사 ID
    private Double hemoglobin; // 헤모글로빈 수치
    private String date; // 검사 날짜

    // 추가적인 필드가 필요하다면 여기에 정의할 수 있습니다.
}
