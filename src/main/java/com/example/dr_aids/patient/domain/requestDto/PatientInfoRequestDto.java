package com.example.dr_aids.patient.domain.requestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PatientInfoRequestDto {

    private String name;
    private Long age;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth; // ex)2003-11-18

    private String gender;

    private String disease;

    private String PIC; //Person In Charge : 담당의사
}
