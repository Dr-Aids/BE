package com.example.dr_aids.weight.service;

import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import com.example.dr_aids.dialysisSession.repository.DialysisSessionRepository;
import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.weight.domain.Weight;
import com.example.dr_aids.weight.domain.requestDto.WeightRequestDto;
import com.example.dr_aids.weight.domain.responseDto.WeightCompareDto;
import com.example.dr_aids.weight.domain.responseDto.WeightCurrentDto;
import com.example.dr_aids.weight.repository.WeightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@AllArgsConstructor
@Service
public class WeightService {
    private final WeightRepository weightRepository;
    private final PatientRepository patientRepository;
    private final DialysisSessionRepository dialysisSessionRepository;


    public void saveWeightInfo(WeightRequestDto requestDto) {
        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다."));

        DialysisSession session = dialysisSessionRepository
                .findByPatient_IdAndSession(patient.getId(), requestDto.getSession())
                .orElseThrow(() -> new IllegalArgumentException("해당 세션을 찾을 수 없습니다."));

        Weight weight = session.getWeight();

        if (weight == null) {
            // ✅ Weight 처음 저장
            weight = Weight.builder()
                    .preWeight(requestDto.getPreWeight())
                    .postWeight(requestDto.getPostWeight())
                    .dryWeight(requestDto.getDryWeight())
                    .targetUF(requestDto.getTargetUF())
                    .controlUF(requestDto.getControlUF())
                    .build();
        } else {
            // ✅ 기존 Weight 수정
            weight.setPreWeight(requestDto.getPreWeight() != null ? requestDto.getPreWeight() : weight.getPreWeight());
            weight.setPostWeight(requestDto.getPostWeight() != null ? requestDto.getPostWeight() : weight.getPostWeight());
            weight.setDryWeight(requestDto.getDryWeight() != null ? requestDto.getDryWeight() : weight.getDryWeight());
            weight.setTargetUF(requestDto.getTargetUF() != null ? requestDto.getTargetUF() : weight.getTargetUF());
            weight.setControlUF(requestDto.getControlUF() != null ? requestDto.getControlUF() : weight.getControlUF());
        }


        weightRepository.save(weight);
        session.setWeight(weight); // 변경이든 새로 생성이든 세션에 연결
        dialysisSessionRepository.save(session);

        // 평균 체중 계산 및 환자 정보 업데이트
        patient.setAverageWeight(calculateAverageWeight(patient));
        patientRepository.save(patient);
    }

    public Double calculateAverageWeight(Patient patient){
        return patient.getDialysisSessions().stream()
                .filter(session -> session.getWeight() != null)
                .filter(session -> session.getWeight().getPreWeight() != null)
                .sorted(Comparator.comparingLong(DialysisSession::getSession).reversed())
                .limit(5)
                .mapToDouble(session -> session.getWeight().getPreWeight())
                .average()
                .orElse(0.0);
    }

    public void deleteWeightInfo(Long patientId, Long session) {
         DialysisSession dialysisSession = dialysisSessionRepository.findByPatient_IdAndSession(patientId, session)
                 .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        Weight weight = dialysisSession.getWeight();

        if (weight != null) {
            weightRepository.delete(weight);
        } else {
            throw new IllegalArgumentException("삭제할 체중 정보가 없습니다.");
        }
    }

    public WeightCurrentDto getCurrentSpecialNote(Long patientId, Long session) {

        DialysisSession currentSession = dialysisSessionRepository
                .findByPatient_IdAndSession(patientId, session)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        return WeightCurrentDto.builder()
                .averageWeight(patient.getAverageWeight())
                .currentWeight(currentSession.getWeight() != null ? currentSession.getWeight().getPreWeight() : null)
                .gap(currentSession.getWeight() != null ?
                        currentSession.getWeight().getPreWeight() - patient.getAverageWeight() : null)
                .build();
    }
    public WeightCompareDto getWeightCompare(Long patientId, Long session) {
        DialysisSession currentSession = dialysisSessionRepository
                .findByPatient_IdAndSession(patientId, session)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        Patient patient = patientRepository
                .findById(patientId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        if(session == 1){
            return WeightCompareDto.builder()
                    .beforePreWeight(currentSession.getWeight().getPreWeight())
                    .gapBetweenBeforeAndNow(0.0)
                    .averageWeight(patient.getAverageWeight())
                    .gapBetweenAverageAndNow(0.0)
                    .build();
        }

        DialysisSession previousSession = dialysisSessionRepository
                .findByPatient_IdAndSession(patientId, session - 1)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        Weight currentWeight = currentSession.getWeight();
        Weight previousWeight = previousSession.getWeight();

        return WeightCompareDto.builder()
                .beforePreWeight(previousWeight != null ? previousWeight.getPreWeight() : null)
                .gapBetweenBeforeAndNow(currentWeight != null && previousWeight != null ?
                        currentWeight.getPreWeight() - previousWeight.getPreWeight() : null)
                .averageWeight(patient.getAverageWeight())
                .gapBetweenAverageAndNow(currentWeight != null ?
                        currentWeight.getPreWeight() - patient.getAverageWeight() : null)
                .build();
    }
}
