package com.example.dr_aids.patient.domain;

import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import com.example.dr_aids.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long age;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String disease;

    private Double averageWeightGain;
    private Double averageWeight;


    private Boolean visiting = false;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DialysisSession> dialysisSessions = new ArrayList<>();


}
