package com.example.dr_aids.hospital.controller;

import com.example.dr_aids.hospital.domain.HospitalNameRequestDto;
import com.example.dr_aids.hospital.service.HospitalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Join", description = "회원가입 API")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/hospital")
public class HospitalController {
    private final HospitalService hospitalService;

    @PostMapping()
    public ResponseEntity<?> saveHospital(@RequestBody HospitalNameRequestDto hospitalNameRequestDto) {
        hospitalService.saveHospital(hospitalNameRequestDto);
        return ResponseEntity.ok("병원 정보가 저장되었습니다.");
    }

    @GetMapping()
    public ResponseEntity<?> getHospitalByHospitalName(@RequestBody HospitalNameRequestDto hospitalNameRequestDto) {
        return ResponseEntity.ok(hospitalService.getHospitalByName(hospitalNameRequestDto));
    }
}
