package com.example.dr_aids.bloodpressure.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BloodPressureNoteDto {
    private String time;
    private String author;  // 작성자 이름 or id
    private String note;
    private Boolean isChecked; // 체크 여부
}
