package com.example.dr_aids.specialNote.domain;

import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String ruleName;
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dialysis_session_id")
    private DialysisSession dialysisSession;
}
