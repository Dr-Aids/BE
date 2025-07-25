package com.example.dr_aids.weight.domain.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WeightCompareDto {
    private Double beforePreWeight; // 이전 체중
    private Double gapBetweenBeforeAndNow;
    private Double averageWeight; // 평균 체중
    private Double gapBetweenAverageAndNow; // 평균 체중과 현재 체중의 차이

}
