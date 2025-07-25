package com.example.dr_aids.dialysisSession.repository;

import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DialysisSessionRepository extends JpaRepository<DialysisSession, Long> {
    Optional<DialysisSession> findByPatient_IdAndSession(Long patientId, Long session);

    List<DialysisSession> findTop2ByPatient_IdAndSessionLessThanEqualOrderBySessionDesc(Long patientId, Long session);


}
