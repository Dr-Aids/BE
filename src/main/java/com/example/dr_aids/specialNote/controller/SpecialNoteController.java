package com.example.dr_aids.specialNote.controller;

import com.example.dr_aids.security.common.CustomUserDetails;
import com.example.dr_aids.specialNote.docs.SpecialNoteDocs;
import com.example.dr_aids.specialNote.service.SpecialNoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/special-note")
@Tag(name = "SpecialNote", description = "특이사항 관련 API")
public class SpecialNoteController implements SpecialNoteDocs {
    private final SpecialNoteService specialNoteService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllSpecialNotes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null || userDetails.getUser() == null) {
            return ResponseEntity.badRequest().body("잘못된 사용자입니다.");
        }

        return ResponseEntity.ok(specialNoteService.getAllSpecialNotes(userDetails.getUser()));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getSpecialNotesByPatientId(
            @PathVariable("patientId") Long patientId) {

        return ResponseEntity.ok(
                specialNoteService.getRecentTwoSpecialNotes(patientId)
        );
    }


}
