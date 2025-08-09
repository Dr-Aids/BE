package com.example.dr_aids.specialNote.docs;

import com.example.dr_aids.security.common.CustomUserDetails;
import com.example.dr_aids.specialNote.domain.SpecialNoteListDto;
import com.example.dr_aids.specialNote.domain.SpecialNotePatientDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/special-note")
public interface SpecialNoteDocs {

    @Operation(
            summary = "오늘자 특이사항 전체 조회",
            description = "로그인 사용자가 속한 병원의 오늘자 특이사항 목록을 조회합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "특이사항 조회 성공",
                            content = @Content(schema = @Schema(implementation = SpecialNoteListDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 사용자 정보"
                    )
            }
    )
    ResponseEntity<?> getAllSpecialNotes(
            @AuthenticationPrincipal
            @Parameter(description = "인증 사용자", required = true)
            CustomUserDetails userDetails
    );

    @Operation(
            summary = "환자별 최근 2회 특이사항 조회",
            description = "특정 환자의 최근 2회 투석 회차에 대한 특이사항 및 주요 지표(SBP/DBP, 전 체중 등)를 조회합니다.",
            parameters = {
                    @Parameter(name = "patientId", description = "환자 ID", required = true, example = "1")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "특이사항 조회 성공",
                            content = @Content(schema = @Schema(implementation = SpecialNotePatientDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "해당 환자/데이터 없음")
            }
    )
    ResponseEntity<?> getSpecialNotesByPatientId(
            @PathVariable("patientId") Long patientId
    );
}
