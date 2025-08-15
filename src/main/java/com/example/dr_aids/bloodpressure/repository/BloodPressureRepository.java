package com.example.dr_aids.bloodpressure.repository;

import com.example.dr_aids.bloodpressure.domain.BloodPressure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BloodPressureRepository extends JpaRepository<BloodPressure, Long> {
}
