package com.example.dr_aids.patient.docs;

import com.example.dr_aids.patient.domain.requestDto.PatientInfoRequestDto;
import com.example.dr_aids.patient.domain.requestDto.PatientVisitindRequestDto;
import com.example.dr_aids.patient.domain.responseDto.PatientInfoResponseDto;
import com.example.dr_aids.patient.domain.responseDto.PatientListResponseDto;
import com.example.dr_aids.security.common.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/patient")
public interface PatientControllerDocs {

    @Operation(
            summary = "환자 정보 저장",
            description = "새로운 환자 정보를 등록합니다.",
            requestBody = @RequestBody(
                    description = "환자 정보 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PatientInfoRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "환자 정보 저장 성공",
                            content = @Content(schema = @Schema(example = "{\"message\": \"환자 정보가 저장되었습니다.\"}")))
            }
    )
    ResponseEntity<?> savePatientInfo(@org.springframework.web.bind.annotation.RequestBody PatientInfoRequestDto dto);

    @Operation(
            summary = "환자 정보 수정",
            description = "환자의 기존 정보를 수정합니다.",
            requestBody = @RequestBody(
                    description = "수정할 환자 정보",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PatientInfoRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "환자 정보 수정 성공",
                            content = @Content(schema = @Schema(example = "{\"message\": \"환자 정보가 수정되었습니다.\"}")))
            }
    )
    ResponseEntity<?> updatePatientInfo(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody PatientInfoRequestDto dto);

    @Operation(summary = "환자 정보 삭제", description = "지정한 환자 정보를 삭제합니다.",
            parameters = {
                    @Parameter(name = "id", description = "환자 ID", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "환자 정보 삭제 성공",
            content = @Content(schema = @Schema(example = "{\"message\": \"환자 삭제가 완료되었습니다.\"}")))
    ResponseEntity<?> deletePatientInfo(@PathVariable Long id);

    @Operation(summary = "환자 정보 조회", description = "ID를 기반으로 환자 상세 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "id", description = "환자 ID", required = true, example = "1")
            })
    @ApiResponse(responseCode = "200", description = "환자 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = PatientInfoResponseDto.class)))
    ResponseEntity<?> getPatientInfo(@PathVariable Long id);

    @Operation(summary = "환자 목록 조회", description = "로그인한 사용자의 병원에 속한 모든 환자 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "환자 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = PatientListResponseDto.class)))
    })
    ResponseEntity<?> getPatientListByHospital(CustomUserDetails userDetails);

    @Operation(
            summary = "환자 방문 여부 수정",
            description = "특정 환자의 당일 방문 여부를 업데이트합니다.",
            parameters = {
                    @Parameter(name = "id", description = "환자 ID", required = true, example = "1")
            },
            requestBody = @RequestBody(
                    description = "방문 여부 수정 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PatientVisitindRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "환자 방문 여부 수정 성공",
                            content = @Content(schema = @Schema(example = "{\"message\": \"환자 방문 여부가 수정되었습니다.\"}")))
            }
    )
    ResponseEntity<?> updatePatientVisitingStatus(@PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody PatientVisitindRequestDto dto);
}
