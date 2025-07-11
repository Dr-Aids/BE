package com.example.dr_aids.user;

import com.example.dr_aids.user.domain.JoinDTO;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import com.example.dr_aids.user.service.JoinService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Transactional
public class JoinTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JoinService joinService;

    @Test
    void 회원가입_성공() {
        // 각 테스트 실행 시 고유한 username과 email을 생성
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "user_" + uniqueSuffix;
        String password = "password";
        String email = "email_" + uniqueSuffix + "@example.com";


        JoinDTO joinDTO = JoinDTO.builder().username(username).password(password)
                .email(email).role("DOCTOR").build();

        Boolean success = joinService.joinProcess(joinDTO);

        Assertions.assertTrue(success, "회원가입은 성공해야 합니다.");

        //DB에 저장된 사용자 정보 확인
        Optional<User> savedUserOptional = userRepository.findByEmail(email);
        Assert.isTrue(savedUserOptional.isPresent(), "저장된 사용자를 찾을 수 없습니다.");
        User savedUser = savedUserOptional.get();
        Assertions.assertEquals(username, savedUser.getUsername(), "사용자 이름이 일치해야 합니다.");
        Assertions.assertEquals(email, savedUser.getEmail(), "이메일이 일치해야 합니다.");
        Assertions.assertEquals("DOCTOR", savedUser.getRole().name(), "역할이 USER여야 합니다.");
        // 추가로 비밀번호가 암호화되어 저장되었는지 확인
        Assertions.assertNotEquals(password, savedUser.getPassword(), "비밀번호는 암호화되어 저장되어야 합니다.");

    }
}
