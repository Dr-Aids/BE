package com.example.dr_aids.dialysisSession.repository;

import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DialysisSessionRepository extends JpaRepository<DialysisSession, Long> {

}
