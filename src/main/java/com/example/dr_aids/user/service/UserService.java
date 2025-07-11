package com.example.dr_aids.user.service;

import com.example.dr_aids.user.domain.Role;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.domain.UserUpdateDTO;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void updateUser(User user, UserUpdateDTO requestDTO) {

        // 수정할 필드만 반영 (null인 건 무시할 수도 있음)
        if (requestDTO.getUsername() != null) {
            user.setUsername(requestDTO.getUsername());
        }
        if (requestDTO.getRole() != null) {
            user.setRole(Role.valueOf(requestDTO.getRole()));
        }
        if (requestDTO.getHospitalName() != null) {
            user.setHospitalName(requestDTO.getHospitalName());
        }

        userRepository.save(user);
    }
}
