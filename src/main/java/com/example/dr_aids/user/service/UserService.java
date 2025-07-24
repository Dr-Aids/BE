package com.example.dr_aids.user.service;

import com.example.dr_aids.user.domain.Role;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.domain.UserInfoResponseDto;
import com.example.dr_aids.user.domain.UserUpdateDTO;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserInfoResponseDto getUserInfo(User user) {
        // 사용자 정보를 조회하여 DTO로 변환
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name()) // Role enum을 문자열로 변환
                .email(user.getEmail())
                .hospitalName(user.getHospital().getHospitalName()) // 병원 이름은 선택적이므로 null일 수 있음
                .build();
    }
    public void updateUser(User user, UserUpdateDTO requestDTO) {

        // 수정할 필드만 반영 (null인 건 무시할 수도 있음)
        if (requestDTO.getUsername() != null) {
            user.setUsername(requestDTO.getUsername());
        }
        if (requestDTO.getRole() != null) {
            user.setRole(Role.valueOf(requestDTO.getRole()));
        }

        userRepository.save(user);
    }
}
