package com.example.dr_aids.dialysisSession.service;

import com.example.dr_aids.bloodpressure.domain.BloodPressure;
import com.example.dr_aids.bloodpressure.domain.BloodPressureDto;
import com.example.dr_aids.bloodpressure.domain.BloodPressureNoteDto;
import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import com.example.dr_aids.dialysisSession.domain.SessionSaveRequestDto;
import com.example.dr_aids.dialysisSession.repository.DialysisSessionRepository;
import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.dialysisSession.domain.SessionDetailRequestDto;
import com.example.dr_aids.patient.domain.responseDto.SessionInfoResponseDto;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.weight.domain.Weight;
import com.example.dr_aids.weight.domain.responseDto.WeightDetailDto;
import com.example.dr_aids.weight.domain.responseDto.WeightTrendDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class DialysisSessionService {
    private final PatientRepository patientRepository;
    private final DialysisSessionRepository dialysisSessionRepository;

    public void addDialysisSessionInfo(SessionSaveRequestDto sessionSaveRequestDto) {
        Patient patient = patientRepository.findById(sessionSaveRequestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        // 현재 환자의 투석 회차 수를 기반으로 다음 회차 번호 생성
        Long session = (long) patient.getDialysisSessions().size() + 1;

        // 새로운 DialysisSession 객체 생성
        DialysisSession dialysisSession = DialysisSession.builder()
                .session(session)
                .date(sessionSaveRequestDto.getDate())
                .patient(patient)
                .build();

        // 몸무게 정보 설정
        Weight weight = Weight.builder()
                .preWeight(sessionSaveRequestDto.getPreWeight())
                .dryWeight(sessionSaveRequestDto.getDryWeight())
                .targetUF(sessionSaveRequestDto.getTargetUF())
                .build();

        // 몸무게 정보와 DialysisSession 연결
        dialysisSession.setWeight(weight);
        patient.getDialysisSessions().add(dialysisSession);

        // 평균 체중 계산 및 환자 정보 업데이트
        patientRepository.save(patient);
        dialysisSessionRepository.save(dialysisSession);
    }
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
