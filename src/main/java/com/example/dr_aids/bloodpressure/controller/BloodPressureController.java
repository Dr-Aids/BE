package com.example.dr_aids.bloodpressure.controller;

import com.example.dr_aids.bloodpressure.domain.requestDto.BPNoteRequestDto;
import com.example.dr_aids.bloodpressure.domain.requestDto.BPSaveRequestDto;
import com.example.dr_aids.bloodpressure.domain.requestDto.BPUpdateRequestDto;
import com.example.dr_aids.bloodpressure.service.BloodPressureService;
import com.example.dr_aids.security.common.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/blood-pressure")
@Tag(name = "BloodPressure", description = "혈압 API")
public class BloodPressureController {
    private final BloodPressureService bloodPressureService;

    @PostMapping()
    public ResponseEntity<?> addBloodPressureInfo(BPSaveRequestDto bloodPressureDto) {
        bloodPressureService.addBloodPressureInfo(bloodPressureDto);
        return ResponseEntity.ok("혈압 정보가 추가되었습니다.");
    }

    @PutMapping()
    public ResponseEntity<?> updateBloodPressureInfo(BPUpdateRequestDto bloodPressureDto) {
        bloodPressureService.updateBloodPressureInfo(bloodPressureDto);
        return ResponseEntity.ok("혈압 정보가 업데이트되었습니다.");
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteBloodPressureInfo(@RequestParam(name = "bloodId") Long bloodId) {
        bloodPressureService.deleteBloodPressureInfo(bloodId);
        return ResponseEntity.ok("혈압 정보가 삭제되었습니다.");
    }

    @PostMapping("/notes")
    public ResponseEntity<?> addBloodPressureNotes(@RequestBody BPNoteRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.badRequest().body("잘못된 사용자입니다.");
        }

        bloodPressureService.addBloodPressureNotes(requestDto, userDetails.getUser());
        return ResponseEntity.ok("혈압 노트가 추가되었습니다.");
    }

    @PutMapping("/notes")
    public ResponseEntity<?> updateBloodPressureNotes(@RequestBody BPNoteRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.badRequest().body("잘못된 사용자입니다.");
        }

        bloodPressureService.updateBloodPressureNotes(requestDto, userDetails.getUser());
        return ResponseEntity.ok("혈압 노트가 업데이트되었습니다.");
    }

    @DeleteMapping("/notes")
    public ResponseEntity<?> deleteBloodPressureNotes(@RequestParam(name = "pressureId") Long pressureId) {
        bloodPressureService.deleteBloodPressureNotes(pressureId);
        return ResponseEntity.ok("혈압 노트가 삭제되었습니다.");
    }
}
