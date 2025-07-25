package com.example.dr_aids.specialNote.service;

import com.example.dr_aids.assignment.repository.AssignmentRepository;
import com.example.dr_aids.bloodpressure.domain.requestDto.BPSaveRequestDto;
import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import com.example.dr_aids.dialysisSession.domain.SessionSaveRequestDto;
import com.example.dr_aids.dialysisSession.repository.DialysisSessionRepository;
import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.patient.domain.Patient;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.specialNote.domain.SpecialNote;
import com.example.dr_aids.specialNote.domain.SpecialNoteDto;
import com.example.dr_aids.specialNote.domain.SpecialNoteListDto;
import com.example.dr_aids.specialNote.domain.SpecialNotePatientDto;
import com.example.dr_aids.specialNote.repository.SpecialNoteRepository;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class SpecialNoteService {
    private final SpecialNoteRepository specialNoteRepository;
    private final PatientRepository patientRepository;
    private final DialysisSessionRepository dialysisSessionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    // 회차 추가 시, 특이사항 확인 후 생성
    /*
    특이사항 기준
    - 이전 투석 후 몸무게보다 증가 :  (기준 3.5kg 이상) / ruleName : PREV_DIFF_OVER_3.5KG
    - 내원 시 평균 몸무게보다 증가 (1kg 이상 차이) / ruleName : AVG_DIFF_OVER_1KG
    - 투석 시작시  혈압이 높을시 : (1단계 : 150이상, 2단계 : 160이상, 3단계 : 180이상)
                                             / ruleName : START_BP_OVER_150
                                             / ruleName : START_BP_OVER_160
                                             / ruleName : START_BP_OVER_180
    - 투석 중에 혈압이 첫 혈압보다 감소 : (처음 혈압대비 30 이상 감소) / ruleName : BP_DROP_OVER_30
    type : weight, bloodPressure
    */
    public void checkSpecialNotesWhenSessionAdd(SessionSaveRequestDto sessionSaveRequestDto, DialysisSession dialysisSession){
        if(dialysisSession.getSession() == 1){ return;}

        Patient patient = patientRepository.findById(sessionSaveRequestDto.getPatientId())
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        DialysisSession previousSession = dialysisSessionRepository.findByPatient_IdAndSession(patient.getId(), dialysisSession.getSession() -1)
                .orElseThrow(() -> new CustomException(ErrorCode.DIALYSIS_SESSION_NOT_FOUND));


        double GapBetweenPrePostWeight = previousSession.getWeight().getPostWeight() - sessionSaveRequestDto.getPreWeight();
        double GapBetweenAvgWeight = patient.getAverageWeight() - sessionSaveRequestDto.getPreWeight();

        // 이전 투석 후 몸무게보다 증가 (기준 3.5kg 이상)
        if(GapBetweenPrePostWeight >= 3.5){
            SpecialNote specialNote = SpecialNote.builder()
                    .type("weight")
                    .value(GapBetweenPrePostWeight)
                    .ruleName("PREV_DIFF_OVER_3.5KG")
                    .build();

            specialNote.setDialysisSession(dialysisSession);
            specialNoteRepository.save(specialNote);
        }
        // 내원 시 평균 몸무게보다 증가 (1kg 이상 차이)
        if(GapBetweenAvgWeight >= 1.0){
            SpecialNote specialNote = SpecialNote.builder()
                    .type("weight")
                    .value(GapBetweenAvgWeight)
                    .ruleName("AVG_DIFF_OVER_1KG")
                    .build();

            specialNote.setDialysisSession(dialysisSession);
            specialNoteRepository.save(specialNote);
        }
    }

    public void checkSpecialNotesWhenBloodPressureAdd(BPSaveRequestDto bloodPressureDto, DialysisSession session) {
        // 투석 시작 시 혈압이 높을시 (1단계 : 150이상, 2단계 : 160이상, 3단계 : 180이상)
        if(session.getBloodPressures().size() == 1){
            if(bloodPressureDto.getSbp() >= 180){
                SpecialNote specialNote = SpecialNote.builder()
                        .type("bloodPressure")
                        .value(bloodPressureDto.getSbp())
                        .ruleName("START_BP_OVER_180")
                        .build();
                specialNote.setDialysisSession(session);
                specialNoteRepository.save(specialNote);
            } else if(bloodPressureDto.getSbp() >= 160){
                SpecialNote specialNote = SpecialNote.builder()
                        .type("bloodPressure")
                        .value(bloodPressureDto.getSbp())
                        .ruleName("START_BP_OVER_160")
                        .build();
                specialNote.setDialysisSession(session);
                specialNoteRepository.save(specialNote);
            } else if(bloodPressureDto.getSbp() >= 150){
                SpecialNote specialNote = SpecialNote.builder()
                        .type("bloodPressure")
                        .value(bloodPressureDto.getSbp())
                        .ruleName("START_BP_OVER_150")
                        .build();
                specialNote.setDialysisSession(session);
                specialNoteRepository.save(specialNote);
            }
        }else {
            // 투석 중에 혈압이 첫 혈압보다 감소 (처음 혈압대비 30 이상 감소)
            double firstBloodPressure = session.getBloodPressures().get(0).getSBP();
            if(bloodPressureDto.getSbp() <= firstBloodPressure - 30){
                SpecialNote specialNote = SpecialNote.builder()
                        .type("bloodPressure")
                        .value(bloodPressureDto.getSbp())
                        .ruleName("BP_DROP_OVER_30")
                        .build();
                specialNote.setDialysisSession(session);
                specialNoteRepository.save(specialNote);
            }
        }
    }
    public List<SpecialNoteListDto> getAllSpecialNotes(User user) {
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        String hospitalName = managedUser.getHospital().getHospitalName();

        List<SpecialNote> specialNotes = specialNoteRepository.findAllByDateAndDoctorHospitalName(today, hospitalName);

        return specialNotes.stream()
                .map(note -> {
                    DialysisSession session = note.getDialysisSession();
                    Patient patient = session.getPatient();

                    // 담당 의사 정보 조회
                    String doctorName = assignmentRepository.findByPatient_Id(patient.getId())
                            .map(assignment -> assignment.getDoctor().getUsername())
                            .orElse("담당자 없음");

                    return SpecialNoteListDto.builder()
                            .id(note.getId())
                            .type(note.getType())
                            .ruleName(note.getRuleName())
                            .value(note.getValue())
                            .PICName(doctorName)
                            .patientName(patient.getName())
                            .session(session.getSession())
                            .date(session.getDate().toString())
                            .build();
                })
                .toList();
    }


    public List<SpecialNotePatientDto> getRecentTwoSpecialNotes(Long patientId) {
        List<SpecialNote> specialNotes = specialNoteRepository.findRecentTwoSessionsNotesByPatientId(patientId);

        return specialNotes.stream()
                .map(note -> {
                    DialysisSession session = note.getDialysisSession();
                    Patient patient = session.getPatient();

                    return SpecialNotePatientDto.builder()
                            .session(session.getSession())
                            .date(session.getDate().toString())
                            .sbp(session.getBloodPressures().isEmpty() ? null : session.getBloodPressures().get(0).getSBP())
                            .dbp(session.getBloodPressures().isEmpty() ? null : session.getBloodPressures().get(0).getDBP())
                            .preWeight(session.getWeight().getPreWeight())
                            .specialNoteDto(SpecialNoteDto.builder()
                                    .id(note.getId())
                                    .type(note.getType())
                                    .ruleName(note.getRuleName())
                                    .value(note.getValue())
                                    .build())
                            .build();
                })
                .toList();
    }

}
