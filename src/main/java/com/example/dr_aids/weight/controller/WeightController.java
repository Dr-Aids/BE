package com.example.dr_aids.weight.controller;

import com.example.dr_aids.weight.docs.WeightControllerDocs;
import com.example.dr_aids.weight.domain.requestDto.WeightRequestDto;
import com.example.dr_aids.weight.service.WeightService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "Weight", description = "체중 관련 API")
@RequestMapping("/weight")
public class WeightController implements WeightControllerDocs {

    private final WeightService weightService;

    @PostMapping() //수정, 삭제
    public ResponseEntity<?> saveWeightInfo(@RequestBody WeightRequestDto requestDto){
        weightService.saveWeightInfo(requestDto);
        return ResponseEntity.ok("체중 정보가 저장되었습니다.");
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteWeightInfo(@RequestParam(name = "patientId") Long patientId,
                                              @RequestParam(name = "session")Long session) {
        weightService.deleteWeightInfo(patientId, session);
        return ResponseEntity.ok("체중 정보가 삭제되었습니다.");
    }

    @GetMapping("/special-note/current")
    public ResponseEntity<?> getCurrentSpecialNote(@RequestParam(name = "patientId") Long patientId,
                                                   @RequestParam(name = "session") Long session) {
        return ResponseEntity.ok(weightService.getCurrentSpecialNote(patientId, session));
    }

    @GetMapping("/special-note/compare")
    public ResponseEntity<?> getWeightCompare(@RequestParam(name = "patientId") Long patientId,
                                                   @RequestParam(name = "session") Long session) {
        return ResponseEntity.ok(weightService.getWeightCompare(patientId, session));
    }

}
