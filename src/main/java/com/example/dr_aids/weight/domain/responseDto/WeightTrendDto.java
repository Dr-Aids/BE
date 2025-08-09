package com.example.dr_aids.weight.domain.responseDto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class WeightTrendDto {
    private Long session; // 세션 회차
    private LocalDate date;
    private Double preWeight;
    private Double dryWeight;
    private Double postWeight;
}

