package com.example.dr_aids.specialNote.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecialNoteDto {
    private Long id; // 특이사항 ID
    private String type; // 특이사항 유형
    private String ruleName; // 규칙 이름
    private double value; // 값
}
