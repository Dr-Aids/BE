package com.example.dr_aids.user.controller;

import com.example.dr_aids.security.common.CustomUserDetails;
import com.example.dr_aids.user.docs.UserControllerDocs;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.domain.UserInfoResponseDto;
import com.example.dr_aids.user.domain.UserUpdateDTO;
import com.example.dr_aids.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 정보 API")
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    private Boolean isValidUser(CustomUserDetails userDetails) {
        return userDetails != null && userDetails.getUser() != null;
    }

    @Override
    @GetMapping()
    public ResponseEntity<?> userinfo(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if(!isValidUser(userDetails)) {
            log.error("잘못된 사용자입니다.");
            return ResponseEntity.badRequest().body("잘못된 사용자입니다.");
        }

        UserInfoResponseDto responseDto = userService.getUserInfo(userDetails.getUser());
        // 사용자 정보 반환
        return ResponseEntity.ok(responseDto);
    }

    @Override
    @PutMapping()
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UserUpdateDTO requestDTO) {
        // 사용자 정보 업데이트 로직 구현
        if(!isValidUser(userDetails)) {
            log.error("잘못된 사용자입니다.");
            return ResponseEntity.badRequest().body("잘못된 사용자입니다.");
        }

        userService.updateUser(userDetails.getUser(), requestDTO);

        log.info("Updating user: {}", userDetails.getUser().getEmail());

        return ResponseEntity.ok( "사용자 정보 수정이 완료되었습니다.");
    }


}
