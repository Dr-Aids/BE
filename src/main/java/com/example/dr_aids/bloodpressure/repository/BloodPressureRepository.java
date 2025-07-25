package com.example.dr_aids.bloodpressure.repository;

import com.example.dr_aids.bloodpressure.domain.BloodPressure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodPressureRepository extends JpaRepository<BloodPressure, Long> {

}
