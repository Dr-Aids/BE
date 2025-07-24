package com.example.dr_aids.weight.docs;

import com.example.dr_aids.weight.domain.requestDto.WeightRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

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
    ResponseEntity<?> deleteWeightInfo(@org.springframework.web.bind.annotation.RequestBody WeightRequestDto requestDto);
}