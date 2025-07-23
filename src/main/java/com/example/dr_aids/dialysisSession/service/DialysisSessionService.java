package com.example.dr_aids.dialysisSession.service;

import com.example.dr_aids.bloodpressure.domain.BloodPressure;
import com.example.dr_aids.bloodpressure.domain.BloodPressureDto;
import com.example.dr_aids.bloodpressure.domain.BloodPressureNoteDto;
import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.dialysisSession.domain.SessionDetailRequestDto;
import com.example.dr_aids.patient.domain.responseDto.SessionInfoResponseDto;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.weight.domain.Weight;
import com.example.dr_aids.weight.domain.WeightDetailDto;
import com.example.dr_aids.weight.domain.WeightTrendDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class DialysisSessionService {
    private final PatientRepository patientRepository;
    public List<SessionInfoResponseDto> getDialysisSessionInfo(Long id){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        List<DialysisSession> dialysisSessions = patient.getDialysisSessions();

        if (dialysisSessions == null || dialysisSessions.isEmpty()) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }

        return dialysisSessions.stream()
                .map(session -> SessionInfoResponseDto.builder()
                        .session(session.getSession())
                        .date(session.getDate())
                        .build())
                .toList();
    }

    public List<WeightDetailDto> getPatientWeightBySession(SessionDetailRequestDto sessionDetailRequestDto) {
        Patient patient = patientRepository.findById(sessionDetailRequestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession currentSession = patient.getDialysisSessions().stream()
                .filter(session -> session.getSession().equals(sessionDetailRequestDto.getSession()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        Weight weight = currentSession.getWeight();

        // 현재 회차 몸무게 정보
        WeightDetailDto weightDetailDto = weight != null ?
                WeightDetailDto.builder()
                        .preWeight(weight.getPreWeight())
                        .averageWeight(patient.getAverageWeight())
                        .dryWeight(weight.getDryWeight())
                        .postWeight(weight.getPostWeight())
                        .targetUF(weight.getTargetUF())
                        .controlUF(weight.getControlUF())
                        .build() : null;

        return List.of(weightDetailDto);

    }

    public List<WeightTrendDto> getPatientWeightTrend(SessionDetailRequestDto sessionDetailRequestDto) {
        Patient patient = patientRepository.findById(sessionDetailRequestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        List<DialysisSession> recentSessions = patient.getDialysisSessions().stream()
                .filter(session -> session.getSession() <= sessionDetailRequestDto.getSession())
                .sorted(Comparator.comparingLong(DialysisSession::getSession)) // 회차 순으로 정렬
                .limit(5) // 최근 5회차만 가져오기
                .toList();

        return recentSessions.stream()
                .map(session -> {
                    Weight weight = session.getWeight();
                    return weight != null ? WeightTrendDto.builder()
                            .session(session.getSession())
                            .date(session.getDate())
                            .preWeight(weight.getPreWeight())
                            .dryWeight(weight.getDryWeight())
                            .postWeight(weight.getPostWeight())
                            .build() : null;
                })
                .toList();
    }

    public List<BloodPressureDto> getPatientBloodPressureBySession(SessionDetailRequestDto sessionDetailRequestDto) {
        Patient patient = patientRepository.findById(sessionDetailRequestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession currentSession = patient.getDialysisSessions().stream()
                .filter(session -> session.getSession().equals(sessionDetailRequestDto.getSession()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        List<BloodPressure> bloodPressures = currentSession.getBloodPressures();

        return bloodPressures.stream()
                .map(bp -> BloodPressureDto.builder()
                        .time(bp.getMeasurementTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .sbp(bp.getSBP())
                        .dbp(bp.getDBP())
                        .build())
                .toList();
    }

    public List<BloodPressureNoteDto> getPatientBloodPressureNotes(SessionDetailRequestDto sessionDetailRequestDto) {
        Patient patient = patientRepository.findById(sessionDetailRequestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession currentSession = patient.getDialysisSessions().stream()
                .filter(session -> session.getSession().equals(sessionDetailRequestDto.getSession()))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        List<BloodPressure> bloodPressures = currentSession.getBloodPressures();

        return bloodPressures.stream()
                .map(bp -> BloodPressureNoteDto.builder()
                        .time(bp.getMeasurementTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                        .note(bp.getNote())
                        .author(bp.getAuthor())
                        .isChecked(bp.getIsChecked())
                        .build())
                .toList();
    }
}
