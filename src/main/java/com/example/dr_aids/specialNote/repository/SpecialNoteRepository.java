package com.example.dr_aids.specialNote.repository;

import com.example.dr_aids.specialNote.domain.SpecialNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SpecialNoteRepository extends JpaRepository<SpecialNote, Long> {
    @Query("""
    SELECT sn FROM SpecialNote sn
    JOIN sn.dialysisSession ds
    JOIN Assignment a ON a.patient = ds.patient
    JOIN User u ON a.doctor = u
    WHERE ds.date = :targetDate AND u.hospital.hospitalName = :hospitalName
    """) // 특정 날짜와 병원 이름에 해당하는 특이사항 조회
    List<SpecialNote> findAllByDateAndDoctorHospitalName(
            @Param("targetDate") LocalDate targetDate,
            @Param("hospitalName") String hospitalName
    );

    @Query(value = """
    SELECT sn.* FROM special_note sn
    JOIN dialysis_session ds ON sn.dialysis_session_id = ds.id
    WHERE ds.patient_id = :patientId
    AND ds.session IN (
        SELECT session FROM dialysis_session
        WHERE patient_id = :patientId
        ORDER BY session DESC
        LIMIT 2
    ) """, nativeQuery = true) // 환자의 최근 두 회차 투석에 대한 특이사항 조회
    List<SpecialNote> findRecentTwoSessionsNotesByPatientId(@Param("patientId") Long patientId);

}
