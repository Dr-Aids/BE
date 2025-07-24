package com.example.dr_aids.dialysisSession.repository;

import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface DialysisSessionRepository extends JpaRepository<DialysisSession, Long> {
    Optional<DialysisSession> findByPatientIdAndSession(Long patientId, Long session);
}
