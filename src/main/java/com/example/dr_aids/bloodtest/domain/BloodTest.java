package com.example.dr_aids.bloodtest.domain;

import com.example.dr_aids.patient.domain.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BloodTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double iron;
    private Double ferritine;
    private Double TIBC;
    private Double PTH;
    private Double hemoglobin;
    private Double hematocrit;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
