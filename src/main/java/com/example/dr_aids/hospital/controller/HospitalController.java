package com.example.dr_aids.hospital.controller;

import com.example.dr_aids.hospital.domain.HospitalListDto;
import com.example.dr_aids.hospital.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Hospital", description = "병원 등록 및 조회 API")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/hospital")
public class HospitalController {
    private final HospitalService hospitalService;

    @Operation(
            summary = "병원 저장",
            description = "새로운 병원 이름을 저장합니다.",
            parameters = {
                    @Parameter(name = "hospitalName", description = "저장할 병원 이름", required = true, example = "서울병원")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "병원 정보 저장 성공",
                            content = @Content(schema = @Schema(example = "\"병원 정보가 저장되었습니다.\"")))
            }
    )
    @PostMapping()
    public ResponseEntity<?> saveHospital(@RequestParam(name = "hospitalName") String hospitalName) {
        hospitalService.saveHospital(hospitalName);
        return ResponseEntity.ok("병원 정보가 저장되었습니다.");
    }

    @Operation(
            summary = "병원 이름으로 조회",
            description = "입력된 병원 이름을 기준으로 병원 목록을 조회합니다. %String% 와 같은 와일드카드 검색이 가능합니다.",
            parameters = {
                    @Parameter(name = "hospitalName", description = "조회할 병원 이름", required = true, example = "서울병원")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "병원 정보 조회 성공",
                            content = @Content(schema = @Schema(implementation = HospitalListDto.class)))
            }
    )
    @GetMapping()
    public ResponseEntity<?> getHospitalByHospitalName(@RequestParam(name = "hospitalName") String hospitalName) {
        return ResponseEntity.ok(hospitalService.getHospitalByName(hospitalName));
    }

    @Operation(
            summary = "병원 목록 조회",
            description = "등록된 모든 병원 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "병원 목록 조회 성공",
                            content = @Content(schema = @Schema(implementation = HospitalListDto.class)))
            }
    )
    @GetMapping("/list")
    public ResponseEntity<?> getAllHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }

}
