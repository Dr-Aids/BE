package com.example.dr_aids.dialysisSession.docs;

import com.example.dr_aids.dialysisSession.domain.SessionDetailRequestDto;
import com.example.dr_aids.patient.domain.responseDto.SessionInfoResponseDto;
import com.example.dr_aids.weight.domain.WeightDetailDto;
import com.example.dr_aids.weight.domain.WeightTrendDto;
import com.example.dr_aids.bloodpressure.domain.BloodPressureDto;
import com.example.dr_aids.bloodpressure.domain.BloodPressureNoteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/session")
public interface DialysisSessionControllerDocs {

    @Operation(summary = "환자의 투석 회차 정보 조회", description = "환자의 모든 투석 회차 일자 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "투석 회차 조회 성공",
            content = @Content(schema = @Schema(implementation = SessionInfoResponseDto.class)))
    ResponseEntity<List<SessionInfoResponseDto>> getPatientSessionInfo(@PathVariable Long id);

    @Operation(summary = "현재 회차 체중 정보 조회", description = "특정 회차의 체중 정보(전후 체중, 건체중 등)를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "체중 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = WeightDetailDto.class)))
    ResponseEntity<List<WeightDetailDto>> getPatientWeightBySession(
            @RequestBody @Parameter(description = "회차 조회를 위한 요청 DTO")
            @Schema(implementation = SessionDetailRequestDto.class) SessionDetailRequestDto dto);

    @Operation(summary = "이전 5회차 체중 변화 조회", description = "최근 5회차의 체중 변화 데이터를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "체중 추세 조회 성공",
            content = @Content(schema = @Schema(implementation = WeightTrendDto.class)))
    ResponseEntity<List<WeightTrendDto>> getPatientWeightTrend(
            @RequestBody @Parameter(description = "체중 추세 조회용 요청 DTO")
            @Schema(implementation = SessionDetailRequestDto.class) SessionDetailRequestDto dto);

    @Operation(summary = "현재 회차 혈압 기록 조회", description = "현재 회차의 시간대별 혈압(SBP/DBP) 기록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "혈압 기록 조회 성공",
            content = @Content(schema = @Schema(implementation = BloodPressureDto.class)))
    ResponseEntity<List<BloodPressureDto>> getPatientBloodPressureBySession(
            @RequestBody @Parameter(description = "혈압 기록 요청 DTO")
            @Schema(implementation = SessionDetailRequestDto.class) SessionDetailRequestDto dto);

    @Operation(summary = "혈압 관련 특이사항 노트 조회", description = "현재 회차의 혈압 관련 특이사항(작성자, 노트 내용 등)을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "혈압 특이사항 조회 성공",
            content = @Content(schema = @Schema(implementation = BloodPressureNoteDto.class)))
    ResponseEntity<List<BloodPressureNoteDto>> getPatientBloodPressureNotes(
            @RequestBody @Parameter(description = "혈압 노트 요청 DTO")
            @Schema(implementation = SessionDetailRequestDto.class) SessionDetailRequestDto dto);

}