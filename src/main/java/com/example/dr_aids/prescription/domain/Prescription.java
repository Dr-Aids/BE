package com.example.dr_aids.prescription.domain;

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
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String hematapoieticAgent; //조혈제
    private Long IU;
    private String description;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
