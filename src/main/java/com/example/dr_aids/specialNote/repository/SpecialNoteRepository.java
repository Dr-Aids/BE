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
        SELECT sn.*
        FROM special_note sn
        JOIN dialysis_session ds
          ON ds.id = sn.dialysis_session_id
        JOIN (
            SELECT id
            FROM dialysis_session
            WHERE patient_id = :patientId
              AND session <= :currentSession
            ORDER BY session DESC
            LIMIT 2
        ) recent
          ON recent.id = ds.id
        WHERE ds.patient_id = :patientId
        ORDER BY ds.session DESC
        """, nativeQuery = true)
    List<SpecialNote> findRecentTwoSessionsNotesByPatientIdAndCurrentSession(
            @Param("patientId") Long patientId,
            @Param("currentSession") Long currentSession
    );

}
