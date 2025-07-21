package com.example.dr_aids.patient.repository;

import com.example.dr_aids.patient.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

}
