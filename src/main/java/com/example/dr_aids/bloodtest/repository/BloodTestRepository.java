package com.example.dr_aids.bloodtest.repository;

import com.example.dr_aids.bloodtest.domain.BloodTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

public interface BloodTestRepository extends JpaRepository<BloodTest, Long> {
    List<BloodTest> findByPatient_IdAndDateBetweenOrderByDateDesc(Long patientId, LocalDate startDate, LocalDate endDate);
}
