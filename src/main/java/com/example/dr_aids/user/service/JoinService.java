package com.example.dr_aids.user.service;

import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.user.domain.JoinDTO;
import com.example.dr_aids.user.domain.Role;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Boolean joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String email = joinDTO.getEmail();
        String role = joinDTO.getRole();

        if(username == null || password == null || email == null || role == null) {
            log.info("joinProcess 실패");
            throw new CustomException(ErrorCode.INVALID_REQUEST);

        }
        Boolean isExist = userRepository.existsByEmail(email);

        if (isExist) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS, "이미 존재하는 이메일입니다.");
        }

        User data = new User();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setEmail(email);
        data.setRole(Role.valueOf(role));

        userRepository.save(data);
        return true;
    }
}

