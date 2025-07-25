package com.example.dr_aids.bloodpressure.service;

import com.example.dr_aids.bloodpressure.domain.BloodPressure;
import com.example.dr_aids.bloodpressure.domain.requestDto.BPNoteRequestDto;
import com.example.dr_aids.bloodpressure.domain.requestDto.BPSaveRequestDto;
import com.example.dr_aids.bloodpressure.domain.requestDto.BPUpdateRequestDto;
import com.example.dr_aids.bloodpressure.domain.responseDto.BloodPressureCurrentDto;
import com.example.dr_aids.bloodpressure.domain.responseDto.BloodPressureDto;
import com.example.dr_aids.bloodpressure.domain.responseDto.BloodPressureRecentDto;
import com.example.dr_aids.bloodpressure.repository.BloodPressureRepository;
import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import com.example.dr_aids.dialysisSession.repository.DialysisSessionRepository;
import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class BloodPressureService {
    private final BloodPressureRepository bloodPressureRepository;
    private final DialysisSessionRepository dialysisSessionRepository;
    private final PatientRepository  patientRepository;
    private final UserRepository userRepository;

    public DialysisSession addBloodPressureInfo(BPSaveRequestDto bloodPressureDto) {
        Patient patient = patientRepository.findById(bloodPressureDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession session = dialysisSessionRepository.findByPatient_IdAndSession(patient.getId(), bloodPressureDto.getSession())
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        BloodPressure bloodPressure = BloodPressure.builder()
                .measurementTime(bloodPressureDto.getMeasurementTime())
                .SBP(bloodPressureDto.getSbp())
                .DBP(bloodPressureDto.getDbp())
                .build();
        bloodPressureRepository.save(bloodPressure);

        session.getBloodPressures().add(bloodPressure);
        dialysisSessionRepository.save(session);

        return session;
    }

    public void updateBloodPressureInfo(BPUpdateRequestDto bloodPressureDto) {
        Patient patient = patientRepository.findById(bloodPressureDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession session = dialysisSessionRepository.findByPatient_IdAndSession(patient.getId(), bloodPressureDto.getSession())
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        BloodPressure bloodPressure = bloodPressureRepository.findById(bloodPressureDto.getBloodId())
                .orElseThrow(() -> new RuntimeException("혈압 정보를 찾을 수 없습니다."));

        if(bloodPressureDto.getMeasurementTime() != null) {
            bloodPressure.setMeasurementTime(bloodPressureDto.getMeasurementTime());
        }
        if(bloodPressureDto.getSbp() != null) {
            bloodPressure.setSBP(bloodPressureDto.getSbp());
        }
        if(bloodPressureDto.getDbp() != null) {
            bloodPressure.setDBP(bloodPressureDto.getDbp());
        }

        bloodPressureRepository.save(bloodPressure);
    }

    public void deleteBloodPressureInfo(Long bloodId) {
        BloodPressure bloodPressure = bloodPressureRepository.findById(bloodId)
                .orElseThrow(() -> new CustomException(ErrorCode.BLOOD_PRESSURE_NOT_FOUND));

        bloodPressureRepository.delete(bloodPressure);
    }

    public void addBloodPressureNotes(BPNoteRequestDto requestDto, User user) {
        User author = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession session = dialysisSessionRepository.findByPatient_IdAndSession(patient.getId(), requestDto.getSession())
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        BloodPressure bloodPressure = bloodPressureRepository.findById(requestDto.getBloodId())
                .orElseThrow(() -> new CustomException(ErrorCode.BLOOD_PRESSURE_NOT_FOUND));

        bloodPressure.setNote(requestDto.getNote());
        bloodPressure.setAuthor(author.getUsername());
        bloodPressure.setIsChecked(false);

        bloodPressureRepository.save(bloodPressure);
    }

    public void updateBloodPressureNotes(BPNoteRequestDto requestDto, User user) {
        User author = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession session = dialysisSessionRepository.findByPatient_IdAndSession(patient.getId(), requestDto.getSession())
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        BloodPressure bloodPressure = bloodPressureRepository.findById(requestDto.getBloodId())
                .orElseThrow(() -> new CustomException(ErrorCode.BLOOD_PRESSURE_NOT_FOUND));

        if(requestDto.getNote() != null){
            bloodPressure.setNote(requestDto.getNote());
        }
        if(requestDto.getIsChecked() != null){
            bloodPressure.setIsChecked(requestDto.getIsChecked());
        }
        bloodPressure.setAuthor(author.getUsername());

        bloodPressureRepository.save(bloodPressure);
    }

    public void deleteBloodPressureNotes(Long pressureId) {
        BloodPressure bloodPressure = bloodPressureRepository.findById(pressureId)
                .orElseThrow(() -> new CustomException(ErrorCode.BLOOD_PRESSURE_NOT_FOUND));

        bloodPressure.setNote(null); // 노트 내용 삭제
        bloodPressure.setAuthor(null); // 작성자 정보 삭제
        bloodPressure.setIsChecked(null); // 체크 여부 초기화
    }
    public BloodPressureCurrentDto getSpecialNoteCurrent(Long patientId, Long session) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession dialysisSession = dialysisSessionRepository.findByPatient_IdAndSession(patient.getId(), session)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        List<BloodPressure> pressures = dialysisSession.getBloodPressures();
        if (pressures.isEmpty()) {
            throw new CustomException(ErrorCode.BLOOD_PRESSURE_NOT_FOUND);
        }

        BloodPressure firstBloodPressure = pressures.get(0);
        BloodPressure lastBloodPressure = pressures.get(pressures.size() - 1);

        return BloodPressureCurrentDto.builder()
                .startSbp(firstBloodPressure.getSBP())
                .startDbp(firstBloodPressure.getDBP())
                .lastSbp(lastBloodPressure.getSBP())
                .lastDbp(lastBloodPressure.getDBP())
                .build();
    }
    public List<BloodPressureRecentDto> getRecentTwoSessionsWithBP(Long patientId, Long session) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        // 1. session 이하 중 가장 최근 2개 회차 조회
        List<DialysisSession> sessions = dialysisSessionRepository
                .findTop2ByPatient_IdAndSessionLessThanEqualOrderBySessionDesc(patientId, session);

        if (sessions.isEmpty()) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }

        // 2. DTO 변환
        return sessions.stream()
                .map(dialysisSession -> BloodPressureRecentDto.builder()
                        .session(dialysisSession.getSession())
                        .bloodPressureDto(
                                dialysisSession.getBloodPressures().stream()
                                        .sorted(Comparator.comparing(BloodPressure::getMeasurementTime))
                                        .map(bp -> BloodPressureDto.builder()
                                                .time(bp.getMeasurementTime().toString())
                                                .sbp(bp.getSBP())
                                                .dbp(bp.getDBP())
                                                .build())
                                        .toList()
                        )
                        .build())
                .toList();
    }
}
