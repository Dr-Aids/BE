package com.example.dr_aids.patient.service;

import com.example.dr_aids.assignment.domain.Assignment;
import com.example.dr_aids.assignment.repository.AssignmentRepository;

import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.hospital.domain.Hospital;
import com.example.dr_aids.patient.domain.*;
import com.example.dr_aids.patient.domain.requestDto.PatientInfoRequestDto;
import com.example.dr_aids.patient.domain.requestDto.PatientVisitindRequestDto;
import com.example.dr_aids.patient.domain.responseDto.PatientInfoResponseDto;
import com.example.dr_aids.patient.domain.responseDto.PatientListResponseDto;
import com.example.dr_aids.patient.repository.PatientRepository;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

            Optional<Assignment> assignmentOptional = assignmentRepository.findByPatient_Id(id);

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

        // 1. DialysisSession 연결 끊기
        patient.getDialysisSessions().clear();

        // 2. Assignment 먼저 삭제
        assignmentRepository.findByPatient_Id(id).ifPresent(assignmentRepository::delete);

        // 3. Patient 삭제
        patientRepository.delete(patient);


    }

    public PatientInfoResponseDto getPatientInfo(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        Optional<Assignment> assignmentOptional = assignmentRepository.findByPatient_Id(id);

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
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        Hospital hospital = managedUser.getHospital();
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

    public void updatePatientVisitingStatus(Long id, PatientVisitindRequestDto patientVisitindRequestDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        patient.setVisiting(patientVisitindRequestDto.getVisiting());
        patientRepository.save(patient);
    }

}
