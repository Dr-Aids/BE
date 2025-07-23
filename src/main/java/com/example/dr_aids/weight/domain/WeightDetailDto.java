package com.example.dr_aids.weight.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeightDetailDto {
    private Double preWeight;
    private Double averageWeight;
    private Double dryWeight;
    private Double postWeight;
    private Double targetUF;
    private Double controlUF;  // 예: ">1kg 초과"
}

