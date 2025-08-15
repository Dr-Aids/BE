package com.example.dr_aids.patient.repository;

import com.example.dr_aids.patient.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT a.patient FROM Assignment a WHERE a.doctor.hospital.hospitalName = :hospitalName")
    List<Patient> findPatientsByHospitalName(@Param("hospitalName") String hospitalName);

}
