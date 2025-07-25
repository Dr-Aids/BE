package com.example.dr_aids.specialNote.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpecialNoteListDto {
    private Long id; // 특이사항 ID
    private String type; // 특이사항 유형
    private String ruleName; // 규칙 이름
    private double value; // 값

    private String PICName; // PIC 이름
    private String patientName; // 환자 이름

    private Long session;
    private String date; // 날짜 (yyyy-MM-dd 형식)


}
