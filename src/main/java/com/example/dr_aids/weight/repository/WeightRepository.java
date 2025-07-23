package com.example.dr_aids.weight.repository;

import com.example.dr_aids.weight.domain.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeightRepository extends JpaRepository<Weight, Long> {

    // 특정 회차 포함 이전 회차들의 모든 Weight 목록 조회
    List<Weight> findByPatientIdAndDialySessionSessionLessThanEqual(Long patientId, Long session);
}
