package com.example.dr_aids.patient.service;

import com.example.dr_aids.assignment.domain.Assignment;
import com.example.dr_aids.assignment.repository.AssignmentRepository;
import com.example.dr_aids.dialysisSession.domain.DialysisSession;
import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.hospital.domain.Hospital;
import com.example.dr_aids.patient.domain.*;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    // 환자 정보를 저장하는 메소드
    public void savePatientInfo(PatientInfoRequestDto patientInfoRequestDto) {

        User doctor = userRepository.findByUsername(patientInfoRequestDto.getPIC())
                .orElseThrow(()-> new CustomException(ErrorCode.DOCTOR_NOT_FOUND)); // 담당의사 설정, 의사가 없을시 오류

        Patient patient = Patient.builder()
                .name(patientInfoRequestDto.getName())
                .age(patientInfoRequestDto.getAge())
                .birth(patientInfoRequestDto.getBirth())
                .gender(patientInfoRequestDto.getGender())
                .disease(patientInfoRequestDto.getDisease())
                .build();
        patientRepository.save(patient);

        Assignment assignment = Assignment.builder()
                .patient(patient)
                .doctor(doctor)
                .build();

        assignmentRepository.save(assignment);
    }

    public void updatePatientInfo(Long id, PatientInfoRequestDto patientInfoRequestDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        // 수정할 필드만 반영
        if (patientInfoRequestDto.getName() != null) {
            patient.setName(patientInfoRequestDto.getName());
        }
        if (patientInfoRequestDto.getAge() != null) {
            patient.setAge(patientInfoRequestDto.getAge());
        }
        if (patientInfoRequestDto.getBirth() != null) {
            patient.setBirth(patientInfoRequestDto.getBirth());
        }
        if (patientInfoRequestDto.getGender() != null) {
            patient.setGender(patientInfoRequestDto.getGender());
        }
        if (patientInfoRequestDto.getDisease() != null) {
            patient.setDisease(patientInfoRequestDto.getDisease());
        }

        if( patientInfoRequestDto.getPIC() != null) {
            User doctor = userRepository.findByUsername(patientInfoRequestDto.getPIC())
                    .orElseThrow(() -> new CustomException(ErrorCode.DOCTOR_NOT_FOUND));

            Optional<Assignment> assignmentOptional = assignmentRepository.findByPatientId(id);

            Assignment assignment;
            if(assignmentOptional.isPresent()){
                assignment = assignmentOptional.get();
                assignment.setDoctor(doctor);
            }else{ // 배정이 안된 경우
                 assignment = Assignment.builder()
                        .patient(patient)
                        .doctor(doctor)
                        .build();
            }
            assignmentRepository.save(assignment);
        }

        patientRepository.save(patient);
    }

    public void deletePatientInfo(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));
        patientRepository.delete(patient);

        // 환자 정보 삭제 시, 해당 환자의 배정 정보도 삭제
        Optional<Assignment> assignmentOptional = assignmentRepository.findByPatientId(id);
        if (assignmentOptional.isPresent()) {
            Assignment assignment = assignmentOptional.get();
            assignmentRepository.delete(assignment);
        }
    }

    public PatientInfoResponseDto getPatientInfo(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));
        Optional<Assignment> assignmentOptional = assignmentRepository.findByPatientId(id);
        String doctorName = assignmentOptional.map(assignment -> assignment.getDoctor().getUsername())
                .orElse(null);
        return PatientInfoResponseDto.builder()
                .id(id)
                .name(patient.getName())
                .age(patient.getAge())
                .disease(patient.getDisease())
                .birth(patient.getBirth())
                .gender(patient.getGender())
                .PIC(doctorName)
                .build();
    }

    public List<PatientListResponseDto> getPatientListByHospital(User user){
        Hospital hospital = user.getHospital();
        List<Patient> patients = patientRepository.findPatientsByHospitalName(hospital.getHospitalName());

        if (patients == null || patients.isEmpty()) {
            throw new CustomException(ErrorCode.PATIENT_NOT_FOUND);
        }

        return patients.stream()
                .map(patient -> PatientListResponseDto.builder()
                        .id(patient.getId())
                        .name(patient.getName())
                        .gender(patient.getGender())
                        .age(patient.getAge())
                        .birth(patient.getBirth())
                        .visiting(patient.getVisiting())
                        .build())
                .toList();
    }

    public List<PatientSessionInfoResponseDto> getPatientDialysisSessionInfo(Long id){
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        List<DialysisSession> dialysisSessions = patient.getDialysisSessions();

        if (dialysisSessions == null || dialysisSessions.isEmpty()) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }

        return dialysisSessions.stream()
                .map(session -> PatientSessionInfoResponseDto.builder()
                        .session(session.getSession())
                        .date(session.getDate())
                        .build())
                .toList();
    }



}
