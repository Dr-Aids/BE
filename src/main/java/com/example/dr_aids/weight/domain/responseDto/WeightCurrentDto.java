package com.example.dr_aids.weight.domain.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeightCurrentDto {
    private Double currentWeight; // 현재 체중
    private Double averageWeight; // 평균 체중
    private Double gap; // 현재 체중과 평균 체중의 차이
}
