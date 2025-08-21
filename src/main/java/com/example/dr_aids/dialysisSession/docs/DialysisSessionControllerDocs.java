package com.example.dr_aids.dialysisSession.docs;


import com.example.dr_aids.dialysisSession.domain.SessionSaveRequestDto;
import com.example.dr_aids.patient.domain.responseDto.SessionInfoResponseDto;
import com.example.dr_aids.weight.domain.responseDto.WeightDetailDto;
import com.example.dr_aids.weight.domain.responseDto.WeightTrendDto;
import com.example.dr_aids.bloodpressure.domain.responseDto.BloodPressureDto;
import com.example.dr_aids.bloodpressure.domain.responseDto.BloodPressureNoteDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/session")
public interface DialysisSessionControllerDocs {

    @Operation(
            summary = "환자 투석 회차 정보 추가",
            description = "환자의 새로운 투석 회차 정보를 추가합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "회차 저장 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SessionSaveRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "투석 회차 추가 성공",
                            content = @Content(schema = @Schema(example = "{\"message\": \"투석 회차 정보가 추가되었습니다.\"}")))
            }
    )
    ResponseEntity<?> addDialysisSessionInfo(@RequestBody SessionSaveRequestDto dto);

    @Operation(
            summary = "환자 투석 회차 정보 삭제",
            description = "특정 환자의 투석 회차 정보를 삭제합니다.",
            parameters = {
                    @Parameter(name = "patientId", description = "환자 ID", required = true),
                    @Parameter(name = "sessionId", description = "삭제할 회차 번호", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "투석 회차 삭제 성공",
                            content = @Content(schema = @Schema(example = "{\"message\": \"투석 회차 정보가 삭제되었습니다.\"}")))
            }
    )
    ResponseEntity<?> deleteDialysisSessionInfo(@PathVariable Long patientId, @PathVariable Long sessionId);

    @Operation(summary = "환자의 투석 회차 정보 조회", description = "환자의 모든 투석 회차 일자 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "투석 회차 조회 성공",
            content = @Content(schema = @Schema(implementation = SessionInfoResponseDto.class)))
    ResponseEntity<?> getPatientSessionInfo(@PathVariable Long id);

    @Operation(
            summary = "현재 회차 체중 정보 조회",
            description = "특정 회차의 체중 정보(전후 체중, 건체중 등)를 반환합니다.",
            parameters = {
                    @Parameter(name = "patientId", description = "환자 ID", required = true),
                    @Parameter(name = "session", description = "조회할 회차 번호", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "체중 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = WeightDetailDto.class)))
            }
    )
    ResponseEntity<?> getWeight(@RequestParam Long patientId, @RequestParam Long session);

    @Operation(
            summary = "이전 5회차 체중 변화 조회",
            description = "최근 5회차의 체중 변화 데이터를 조회합니다.",
            parameters = {
                    @Parameter(name = "patientId", description = "환자 ID", required = true),
                    @Parameter(name = "session", description = "조회 기준 회차 번호", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "체중 추세 조회 성공",
                            content = @Content(schema = @Schema(implementation = WeightTrendDto.class)))
            }
    )
    ResponseEntity<?> getWeightTrend(@RequestParam Long patientId, @RequestParam Long session);

    @Operation(
            summary = "현재 회차 혈압 기록 조회",
            description = "현재 회차의 시간대별 혈압(SBP/DBP) 기록을 반환합니다.",
            parameters = {
                    @Parameter(name = "patientId", description = "환자 ID", required = true),
                    @Parameter(name = "session", description = "회차 번호", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "혈압 기록 조회 성공",
                            content = @Content(schema = @Schema(implementation = BloodPressureDto.class)))
            }
    )
    ResponseEntity<?> getBloodPressure(@RequestParam Long patientId, @RequestParam Long session);

    @Operation(
            summary = "혈압 관련 특이사항 노트 조회",
            description = "현재 회차의 혈압 관련 특이사항(작성자, 노트 내용 등)을 조회합니다.",
            parameters = {
                    @Parameter(name = "patientId", description = "환자 ID", required = true),
                    @Parameter(name = "session", description = "회차 번호", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "혈압 특이사항 조회 성공",
                            content = @Content(schema = @Schema(implementation = BloodPressureNoteDto.class)))
            }
    )
    ResponseEntity<?> getBloodPressureNotes(@RequestParam Long patientId, @RequestParam Long session);
}
