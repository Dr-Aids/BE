package com.example.dr_aids.dialysisSession.domain;

import com.example.dr_aids.bloodpressure.domain.BloodPressure;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.specialNote.domain.SpecialNote;
import com.example.dr_aids.weight.domain.Weight;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DialysisSession { //투석회차
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long session;
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "weight_id")
    private Weight weight;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dialysis_session_id")
    private List<BloodPressure> bloodPressures = new ArrayList<>();
}
