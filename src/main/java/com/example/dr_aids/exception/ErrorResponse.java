package com.example.dr_aids.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "에러 응답")
public class ErrorResponse {

    @Schema(description = "HTTP 상태 코드", example = "409")
    private int status;

    @Schema(description = "에러 코드 이름", example = "USER_ALREADY_EXISTS")
    private String code;

    @Schema(description = "에러 메시지", example = "이미 존재하는 사용자입니다")
    private String message;

    public ErrorResponse(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
