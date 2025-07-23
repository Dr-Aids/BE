package com.example.dr_aids.user.service;

import com.example.dr_aids.exception.CustomException;
import com.example.dr_aids.exception.ErrorCode;
import com.example.dr_aids.hospital.domain.Hospital;
import com.example.dr_aids.hospital.repository.HospitalRepository;
import com.example.dr_aids.user.domain.JoinDTO;
import com.example.dr_aids.user.domain.Role;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@AllArgsConstructor
public class JoinService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final HospitalRepository hospitalRepository;

    public Boolean joinProcess(JoinDTO joinDTO) {
        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String email = joinDTO.getEmail();
        String role = joinDTO.getRole();
        String hospitalname = joinDTO.getHospitalname();

        if(username == null || password == null || email == null || role == null || hospitalname == null) {
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
        //병원 추가
        Optional<Hospital> hospital = hospitalRepository.findByHospitalName(hospitalname);

        if (hospital.isEmpty()) {
            log.info("joinProcess 실패: 병원이 존재하지 않습니다.");
            throw new CustomException(ErrorCode.INVALID_REQUEST, "존재하지 않는 병원입니다.");
        }
        data.setHospital(hospital.get());
        userRepository.save(data);
        return true;
    }
}

