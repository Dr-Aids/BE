package com.example.dr_aids.assignment.repository;

import com.example.dr_aids.assignment.domain.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    Optional<Assignment> findByPatient_Id(Long patientId);
}
