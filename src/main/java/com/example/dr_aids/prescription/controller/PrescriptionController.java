package com.example.dr_aids.prescription.controller;

import com.example.dr_aids.prescription.domain.PrescriptionDto;
import com.example.dr_aids.prescription.service.PrescriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/ai")
    public ResponseEntity<?> savePrescription(@RequestBody PrescriptionDto prescriptionDto) {
        prescriptionService.savePrescription(prescriptionDto);
        return ResponseEntity.ok("처방전이 저장되었습니다.");
    }

    @GetMapping("/{patientId}/{targetDate}")
    public ResponseEntity<?> getPrescriptions(@RequestParam(name = "patientId") Long patientId,
                                              @RequestParam(name = "targetDate") String targetDate) {
        return ResponseEntity.ok(prescriptionService.getPrescriptions(patientId, targetDate));
    }

}
