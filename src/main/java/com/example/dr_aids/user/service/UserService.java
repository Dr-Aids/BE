package com.example.dr_aids.user.service;

import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.domain.UserUpdateDTO;
import com.example.dr_aids.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User updateUser(User user, UserUpdateDTO requestDTO) {

        // 수정할 필드만 반영 (null인 건 무시할 수도 있음)
        if (requestDTO.getUsername() != null) {
            user.setUsername(requestDTO.getUsername());
        }
        if (requestDTO.getEmail() != null) {
            user.setEmail(requestDTO.getEmail());
        }

        return userRepository.save(user);
    }
}
