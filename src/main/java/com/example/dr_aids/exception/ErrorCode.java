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
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "세션을 찾을 수 없습니다"),
    BLOOD_PRESSURE_NOT_FOUND(HttpStatus.NOT_FOUND, "혈압 정보를 찾을 수 없습니다"),

    //회차 관련 오류
    DIALYSIS_SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "투석 회차를 찾을 수 없습니다"),


    // 의사 관련 오류,
    DOCTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "의사를 찾을 수 없습니다"),

    //병원 관련 오류
    HOSPITAL_NOT_FOUND(HttpStatus.NOT_FOUND, "병원을 찾을 수 없습니다"),
    HOSPITAL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 병원입니다"),
    HOSPITAL_NAME_TOO_SHORT(HttpStatus.BAD_REQUEST, "병원 이름은 최소 2자 이상이어야 합니다"),
    HOSPITAL_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "병원 이름은 필수 입력 항목입니다"),

    // 처방전 관련 오류
    PRESCRIPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "처방전을 찾을 수 없습니다"),;



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
