package com.example.dr_aids.user.controller;

import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.exception.ErrorResponse;
import com.example.dr_aids.user.domain.JoinDTO;
import com.example.dr_aids.user.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
@Tag(name = "Join", description = "회원가입 API")
@Slf4j
@RestController
public class JoinController {
    private final JoinService joinService;

    public  JoinController(JoinService joinService){
        this.joinService = joinService;
    }

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody JoinDTO joinDTO){
        boolean success = joinService.joinProcess(joinDTO);
        if (success) {
            return ResponseEntity.ok(Collections.singletonMap("message", "success"));
        } else {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS); // 기존 방식 대신 예외 던지기
        }
    }

}
