package com.example.dr_aids.user.controller;

import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.user.domain.JoinDTO;
import com.example.dr_aids.user.service.JoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@Slf4j
@RestController
public class JoinController {
    private final JoinService joinService;

    public  JoinController(JoinService joinService){
        this.joinService = joinService;
    }

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
