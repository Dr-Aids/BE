package com.example.dr_aids.bloodtest.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BloodTestAllResponseDto {
    private Long id; // 검사 ID
    private String date; // 검사 날짜
    private Double iron; // 철분 수치
    private Double ferritine; // 페리틴 수치
    private Double TIBC; // 총 철결합능
    private Double PTH; // 부갑상선 호르몬 수치
    private Double hematocrit; // 헤마토크릿 수치
}
