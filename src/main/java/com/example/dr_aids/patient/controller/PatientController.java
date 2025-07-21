package com.example.dr_aids.patient.controller;

import com.example.dr_aids.patient.domain.PatientInfoRequestDto;
import com.example.dr_aids.patient.service.PatientService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Patient", description = "환자 정보 API")
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    @PostMapping("/info")
    public ResponseEntity<?> savePatientInfo(@RequestBody PatientInfoRequestDto patientInfoRequestDto) {

        patientService.savePatientInfo(patientInfoRequestDto);
        return ResponseEntity.ok("Patient information saved successfully");
    }

    @PutMapping("/info/{id}")
    public ResponseEntity<?> updatePatientInfo(@PathVariable Long id, @RequestBody PatientInfoRequestDto patientInfoRequestDto) {
        patientService.updatePatientInfo(id, patientInfoRequestDto);
        return ResponseEntity.ok("Patient information updated successfully");
    }

    @DeleteMapping("/info/{id}")
    public ResponseEntity<?> deletePatientInfo(@PathVariable Long id) {
        patientService.deletePatientInfo(id);
        return ResponseEntity.ok("Patient information deleted successfully");
    }


}
