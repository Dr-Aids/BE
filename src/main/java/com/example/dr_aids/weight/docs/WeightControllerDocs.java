package com.example.dr_aids.weight.docs;

import com.example.dr_aids.weight.domain.requestDto.WeightRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/weight")
public interface WeightControllerDocs {
    @Operation(
            summary = "체중 정보 저장/수정",
            description = "회차별 환자의 체중 정보를 등록하거나 수정합니다.",
            requestBody = @RequestBody(
                    description = "체중 정보 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WeightRequestDto.class))
            )
    )
    @ApiResponse(responseCode = "200", description = "체중 정보 저장 성공",
            content = @Content(schema = @Schema(example = "{\"message\": \"체중 정보가 저장되었습니다.\"}")))
    ResponseEntity<?> saveWeightInfo(@org.springframework.web.bind.annotation.RequestBody WeightRequestDto requestDto);

    @Operation(
            summary = "체중 정보 삭제",
            description = "회차별 환자의 체중 정보를 삭제합니다.",
            requestBody = @RequestBody(
                    description = "삭제할 체중 정보 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = WeightRequestDto.class))
            )
    )
    @ApiResponse(responseCode = "200", description = "체중 정보 삭제 성공",
            content = @Content(schema = @Schema(example = "{\"message\": \"체중 정보가 삭제되었습니다.\"}")))
    ResponseEntity<?> deleteWeightInfo(@RequestParam(name = "patientId") Long patientId,
                                       @RequestParam(name = "session") Long session);
    @Operation(
            summary = "현재 세션의 특이사항 조회",
            description = "회차별 환자의 현재 세션에 대한 특이사항을 조회합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(name = "patientId", description = "환자 ID", required = true),
                    @io.swagger.v3.oas.annotations.Parameter(name = "session", description = "세션 회차", required = true)
            }
    )
    @ApiResponse(responseCode = "200", description = "특이사항 조회 성공",
            content = @Content(schema = @Schema(example = "{\"specialNote\": \"특이사항 내용\"}")))
    ResponseEntity<?> getCurrentSpecialNote(@RequestParam(name = "patientId") Long patientId,
                                            @RequestParam(name = "session") Long session);
    @Operation(
            summary = "체중 비교 정보 조회",
            description = "회차별 환자의 현재 회차와 이전 1회차의 체중 비교 정보를 조회합니다.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(name = "patientId", description = "환자 ID", required = true),
                    @io.swagger.v3.oas.annotations.Parameter(name = "session", description = "세션 회차", required = true)
            }
    )
    @ApiResponse(responseCode = "200", description = "체중 비교 정보 조회 성공",
            content = @Content(schema = @Schema(example = "{\"preWeight\": 70.0, \"postWeight\": 68.0, \"dryWeight\": 67.5}")))
    ResponseEntity<?> getWeightCompare(@RequestParam(name = "patientId") Long patientId,
                                       @RequestParam(name = "session") Long session);
}