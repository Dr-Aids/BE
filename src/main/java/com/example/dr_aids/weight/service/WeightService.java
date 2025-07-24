package com.example.dr_aids.weight.service;

import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import com.example.dr_aids.dialysisSession.repository.DialysisSessionRepository;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.weight.domain.Weight;
import com.example.dr_aids.weight.domain.requestDto.WeightRequestDto;
import com.example.dr_aids.weight.repository.WeightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
                .findByPatientIdAndSession(patient.getId(), requestDto.getSession())
                .orElseThrow(() -> new IllegalArgumentException("해당 세션을 찾을 수 없습니다."));

        // ✅ 평균 체중 계산
        Double averageWeight = patient.getAverageWeight() !=  null ?
                (patient.getAverageWeight() + requestDto.getPreWeight()) / 2 : requestDto.getPreWeight();
        patient.setAverageWeight(averageWeight);

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
            weight.setPreWeight(requestDto.getPreWeight());
            weight.setPostWeight(requestDto.getPostWeight());
            weight.setDryWeight(requestDto.getDryWeight());
            weight.setTargetUF(requestDto.getTargetUF());
            weight.setControlUF(requestDto.getControlUF());
        }


        weightRepository.save(weight);
        session.setWeight(weight); // 변경이든 새로 생성이든 세션에 연결
        dialysisSessionRepository.save(session);
    }

    public void deleteWeightInfo(WeightRequestDto requestDto) {
        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("환자를 찾을 수 없습니다."));

        DialysisSession session = dialysisSessionRepository.findById(requestDto.getSession())
                .orElseThrow(() -> new IllegalArgumentException("해당 세션을 찾을 수 없습니다."));

        Weight weight = session.getWeight();
        if (weight != null) {
            weightRepository.delete(weight);
            session.setWeight(null); // 세션에서 Weight 제거
            dialysisSessionRepository.save(session);
        } else {
            throw new IllegalArgumentException("삭제할 체중 정보가 없습니다.");
        }
    }

}
