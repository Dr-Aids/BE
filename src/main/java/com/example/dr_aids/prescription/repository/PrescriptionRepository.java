package com.example.dr_aids.prescription.repository;

import com.example.dr_aids.prescription.domain.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatient_IdAndDateBetweenOrderByDateDesc(
            Long patientId, LocalDate startDate, LocalDate endDate
    );
}
