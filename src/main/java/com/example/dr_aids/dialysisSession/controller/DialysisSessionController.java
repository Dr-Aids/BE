package com.example.dr_aids.dialysisSession.controller;

import com.example.dr_aids.dialysisSession.service.DialysisSessionService;
import com.example.dr_aids.dialysisSession.domain.SessionDetailRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "DialysisSession", description = "환자 투석 회차 API")
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/session")
public class DialysisSessionController {

    private final DialysisSessionService dialysisSessionService;

    @GetMapping("/info/session/{id}") // 환자의 투석 회차 조회
    public ResponseEntity<?> getPatientSessionInfo(@PathVariable Long id) {
        return ResponseEntity.ok(dialysisSessionService.getDialysisSessionInfo(id));
    }

    @GetMapping("/info/session/weight") // 환자 현재 회차 체중 조회
    public ResponseEntity<?> getPatientWeightBySession(@RequestBody SessionDetailRequestDto sessionDetailRequestDto) {
        return ResponseEntity.ok(dialysisSessionService.getPatientWeightBySession(sessionDetailRequestDto));
    }

    @GetMapping("/info/session/weights") // 환자의 이전 5회차 체중 기록 조회)
    public ResponseEntity<?> getPatientWeightTrend(@RequestBody SessionDetailRequestDto sessionDetailRequestDto) {
        return ResponseEntity.ok(dialysisSessionService.getPatientWeightTrend(sessionDetailRequestDto));
    }

    @GetMapping("/info/session/bps") // 환자의 현재 회차 혈압 기록 조회
    public ResponseEntity<?> getPatientBloodPressureBySession(@RequestBody SessionDetailRequestDto sessionDetailRequestDto) {
        return ResponseEntity.ok(dialysisSessionService.getPatientBloodPressureBySession(sessionDetailRequestDto));
    }

    @GetMapping("/info/session/bpnotes") // 환자의 현재 회차 혈압 기록 노트 조회
    public ResponseEntity<?> getPatientBloodPressureNotes(@RequestBody SessionDetailRequestDto sessionDetailRequestDto) {
        return ResponseEntity.ok(dialysisSessionService.getPatientBloodPressureNotes(sessionDetailRequestDto));

    }

}
