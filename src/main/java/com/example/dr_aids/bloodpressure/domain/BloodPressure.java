package com.example.dr_aids.bloodpressure.domain;

import com.example.dr_aids.util.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BloodPressure extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long SBP; // Systolic Blood Pressure
    private Long DBP; // Diastolic Blood Pressure

    //측정 시간
    private LocalDateTime measurementTime;

    private String note;
    private String writer;

}
