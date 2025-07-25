package com.example.dr_aids.dialysisSession.repository;

import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DialysisSessionRepository extends JpaRepository<DialysisSession, Long> {
    Optional<DialysisSession> findByPatient_IdAndSession(Long patientId, Long session);

}
