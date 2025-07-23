package com.example.dr_aids.patient.controller;

import com.example.dr_aids.patient.docs.PatientControllerDocs;
import com.example.dr_aids.patient.domain.requestDto.PatientInfoRequestDto;
import com.example.dr_aids.patient.domain.requestDto.PatientVisitindRequestDto;
import com.example.dr_aids.patient.service.PatientService;
import com.example.dr_aids.security.common.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Patient", description = "환자 정보 API")
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/patient")
public class PatientController implements PatientControllerDocs {

    private final PatientService patientService;

    @PostMapping("/info") // 환자 정보 저장
    public ResponseEntity<?> savePatientInfo(@RequestBody PatientInfoRequestDto patientInfoRequestDto) {

        patientService.savePatientInfo(patientInfoRequestDto);
        return ResponseEntity.ok("환자 정보가 저장되었습니다.");
    }

    @PutMapping("/info/{id}") // 환자 정보 수정
    public ResponseEntity<?> updatePatientInfo(@PathVariable Long id, @RequestBody PatientInfoRequestDto patientInfoRequestDto) {
        patientService.updatePatientInfo(id, patientInfoRequestDto);
        return ResponseEntity.ok("환자 정보가 수정되었습니다.");
    }

    @DeleteMapping("/info/{id}") // 환자 정보 삭제
    public ResponseEntity<?> deletePatientInfo(@PathVariable Long id) {
        patientService.deletePatientInfo(id);
        return ResponseEntity.ok("환자 삭제가 완료되었습니다.");
    }

    @GetMapping("/info/{id}") // 환자 정보 조회
    public ResponseEntity<?> getPatientInfo(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientInfo(id));
    }

    @GetMapping("/list") // 환자 목록 조회
    public ResponseEntity<?> getPatientListByHospital(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.badRequest().body("사용자 정보가 없습니다.");
        }

        return ResponseEntity.ok(patientService.getPatientListByHospital(userDetails.getUser()));
    }

    @PutMapping("/info/visiting/{id}") // 환자 방문 여부 수정
    public ResponseEntity<?> updatePatientVisitingStatus(@PathVariable Long id, @RequestBody PatientVisitindRequestDto patientVisitindRequestDto) {
        patientService.updatePatientVisitingStatus(id, patientVisitindRequestDto);
        return ResponseEntity.ok("환자 방문 여부가 수정되었습니다.");
    }
}
