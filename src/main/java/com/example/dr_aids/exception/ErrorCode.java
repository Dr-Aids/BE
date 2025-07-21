package com.example.dr_aids.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(501, "서버 내부 오류가 발생했습니다: %s"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청이 들어왔습니다"),

    // 사용자, 인증 관련 오류
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다"),
    INVALID_EMAIL(HttpStatus.UNAUTHORIZED, "잘못된 이메일입니다"),

    // 환자 관련 오류
    PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "환자를 찾을 수 없습니다"),

    // 의사 관련 오류
    DOCTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "의사를 찾을 수 없습니다");

    private final String message;
    private final int status;

    // int 생성자
    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // HttpStatus 생성자
    ErrorCode(HttpStatus httpStatus, String message) {
        this.status = httpStatus.value();
        this.message = message;
    }

}
