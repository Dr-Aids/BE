package com.example.dr_aids.hospital.repository;

import com.example.dr_aids.hospital.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    //hospitalName이 들어가 있는 이름의 병원들 조회
    @Query("SELECT h FROM Hospital h WHERE h.hospitalName LIKE %:hospitalName%")
    List<Hospital> findAllByHospitalName(@Param("hospitalName")String hospitalName);

    Optional<Hospital> findByHospitalName(String hospitalname);
}
