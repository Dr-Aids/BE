package com.example.dr_aids.user.docs;

import com.example.dr_aids.security.common.CustomUserDetails;
import com.example.dr_aids.user.domain.UserInfoResponseDto;
import com.example.dr_aids.user.domain.UserUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserControllerDocs {

    @Operation(
            summary = "현재 사용자 정보 조회",
            description = "JWT 인증을 통해 로그인된 사용자의 정보를 반환합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 반환 성공",
                    content = @Content(schema = @Schema(implementation = UserInfoResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "사용자 인증 정보 없음",
                    content = @Content(schema = @Schema(
                            example = "{\"error\": \"잘못된 사용자입니다.\"}"
                    ))
            )
    })
    ResponseEntity<?> userinfo(CustomUserDetails userDetails);

    @Operation(
            summary = "사용자 정보 수정",
            description = "요청된 정보를 바탕으로 사용자 정보를 수정합니다. 인증된 사용자만 수행할 수 있습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공",
                    content = @Content(schema = @Schema(
                            example = "{\"message\": \"사용자 정보 수정이 완료되었습니다.\"}"
                    ))
            ),
            @ApiResponse(responseCode = "400", description = "인증 정보 누락 또는 유효하지 않은 요청",
                    content = @Content(schema = @Schema(
                            example = "{\"error\": \"잘못된 사용자입니다.\"}"
                    ))
            )
    })
    ResponseEntity<?> updateUser(CustomUserDetails userDetails, @RequestBody UserUpdateDTO requestDTO);
}
