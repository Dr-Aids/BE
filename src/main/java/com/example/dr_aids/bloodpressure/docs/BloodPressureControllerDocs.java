package com.example.dr_aids.bloodpressure.docs;

import com.example.dr_aids.bloodpressure.domain.requestDto.BPNoteRequestDto;
import com.example.dr_aids.bloodpressure.domain.requestDto.BPSaveRequestDto;
import com.example.dr_aids.bloodpressure.domain.requestDto.BPUpdateRequestDto;
import com.example.dr_aids.security.common.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/blood-pressure")
public interface BloodPressureControllerDocs {

    @Operation(
            summary = "혈압 정보 등록",
            description = "환자의 특정 투석 회차에 혈압 정보를 등록합니다.",
            requestBody = @RequestBody(
                    description = "혈압 정보 등록 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BPSaveRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "혈압 정보 등록 성공"),
                    @ApiResponse(responseCode = "404", description = "환자 또는 투석 회차를 찾을 수 없음")
            }
    )
    ResponseEntity<?> addBloodPressureInfo(@RequestBody BPSaveRequestDto bloodPressureDto);

    @Operation(
            summary = "혈압 정보 수정",
            description = "기존 혈압 정보(SBP, DBP, 측정시간)를 수정합니다.",
            requestBody = @RequestBody(
                    description = "혈압 정보 수정 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BPUpdateRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "혈압 정보 수정 성공"),
                    @ApiResponse(responseCode = "404", description = "혈압 정보를 찾을 수 없음")
            }
    )
    ResponseEntity<?> updateBloodPressureInfo(@RequestBody BPUpdateRequestDto bloodPressureDto);

    @Operation(
            summary = "혈압 정보 삭제",
            description = "혈압 ID를 기반으로 해당 혈압 정보를 삭제합니다.",
            parameters = {
                    @Parameter(name = "bloodId", description = "삭제할 혈압 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "혈압 정보 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "혈압 정보를 찾을 수 없음")
            }
    )
    ResponseEntity<?> deleteBloodPressureInfo(@RequestParam(name = "bloodId") Long bloodId);

    @Operation(
            summary = "혈압 노트 등록",
            description = "혈압 정보에 대해 특이사항 노트를 등록합니다. 작성자는 로그인 사용자로 기록됩니다.",
            requestBody = @RequestBody(
                    description = "혈압 노트 등록 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BPNoteRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "혈압 노트 등록 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 사용자 정보")
            }
    )
    ResponseEntity<?> addBloodPressureNotes(@RequestBody BPNoteRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(
            summary = "혈압 노트 수정",
            description = "혈압 노트 내용 또는 체크 여부를 수정합니다.",
            requestBody = @RequestBody(
                    description = "혈압 노트 수정 요청 DTO",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BPNoteRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "혈압 노트 수정 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 사용자 정보 또는 요청")
            }
    )
    ResponseEntity<?> updateBloodPressureNotes(@RequestBody BPNoteRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(
            summary = "혈압 노트 삭제",
            description = "혈압 정보에 등록된 노트를 삭제합니다.",
            parameters = {
                    @Parameter(name = "pressureId", description = "노트를 삭제할 혈압 ID", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "혈압 노트 삭제 성공"),
                    @ApiResponse(responseCode = "404", description = "해당 혈압 정보를 찾을 수 없음")
            }
    )
    ResponseEntity<?> deleteBloodPressureNotes(@RequestParam(name = "pressureId") Long pressureId);
}
