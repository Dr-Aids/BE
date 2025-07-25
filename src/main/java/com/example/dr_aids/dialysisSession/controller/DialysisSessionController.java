package com.example.dr_aids.dialysisSession.controller;

import com.example.dr_aids.dialysisSession.docs.DialysisSessionControllerDocs;
import com.example.dr_aids.dialysisSession.domain.SessionSaveRequestDto;
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
public class DialysisSessionController implements DialysisSessionControllerDocs {

    private final DialysisSessionService dialysisSessionService;

    @PostMapping("") // 환자 투석 회차 정보 추가
    public ResponseEntity<?> addDialysisSessionInfo(@RequestBody SessionSaveRequestDto sessionSaveRequestDto) {
        dialysisSessionService.addDialysisSessionInfo(sessionSaveRequestDto);
        return ResponseEntity.ok("투석 회차 정보가 추가되었습니다.");
    }
    @GetMapping("/{id}") // 환자의 투석 회차 조회
    public ResponseEntity<?> getPatientSessionInfo(@PathVariable Long id) {
        return ResponseEntity.ok(dialysisSessionService.getDialysisSessionInfo(id));
    }

    @GetMapping("/weight") // 현재 회차 체중 정보 조회
    public ResponseEntity<?> getWeight(@RequestParam(name = "patientId") Long patientId, @RequestParam(name = "session") Long session) {
        return ResponseEntity.ok(dialysisSessionService.getPatientWeightBySession(patientId, session));
    }

    @GetMapping("/weights") // 이전 5회차 체중 변화 조회
    public ResponseEntity<?> getWeightTrend(@RequestParam(name = "patientId") Long patientId, @RequestParam(name = "session") Long session) {
        return ResponseEntity.ok(dialysisSessionService.getPatientWeightTrend(patientId, session));
    }

    @GetMapping("/bps") // 현재 회차 혈압 정보 조회
    public ResponseEntity<?> getBloodPressure(@RequestParam(name = "patientId") Long patientId, @RequestParam(name = "session") Long session) {
        return ResponseEntity.ok(dialysisSessionService.getPatientBloodPressureBySession(patientId, session));
    }

    @GetMapping("/bpnotes") // 현재 회차 혈압 노트 정보 조회
    public ResponseEntity<?> getBloodPressureNotes(@RequestParam(name = "patientId") Long patientId, @RequestParam(name = "session") Long session) {
        return ResponseEntity.ok(dialysisSessionService.getPatientBloodPressureNotes(patientId, session));
    }



}
