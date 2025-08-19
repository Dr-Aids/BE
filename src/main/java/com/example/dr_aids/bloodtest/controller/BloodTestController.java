package com.example.dr_aids.bloodtest.controller;

import com.example.dr_aids.bloodtest.domain.BloodTestDto;
import com.example.dr_aids.bloodtest.service.BloodTestService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("blood-test")
public class BloodTestController {
    private final BloodTestService bloodTestService;

    @PostMapping("/ai")
    public ResponseEntity<?> saveBloodTest(@RequestBody List<BloodTestDto> bloodTestDto){
        bloodTestService.saveBloodTests(bloodTestDto);
        return ResponseEntity.ok("혈액검사 저장 완료되었습니다.");
    }

    @GetMapping("/{patientId}/{targetDate}/all")
    public ResponseEntity<?> getBloodTestAll(@RequestParam(name = "patientId") Long patientId,
                                             @RequestParam(name = "targetDate") String targetDate){
        return ResponseEntity.ok(bloodTestService.getBloodTestAll(patientId, targetDate));
    }

    @GetMapping("/{patientId}/{targetDate}/only-hb")
    public ResponseEntity<?> getBloodTestOnlyHb(@RequestParam(name = "patientId") Long patientId,
                                                @RequestParam(name = "targetDate") String targetDate){
        return ResponseEntity.ok(bloodTestService.getBloodTestOnlyHb(patientId, targetDate));
    }
}
