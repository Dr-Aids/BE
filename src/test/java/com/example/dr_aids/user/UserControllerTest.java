package com.example.dr_aids.user;

import com.example.dr_aids.security.common.CustomUserDetails;
import com.example.dr_aids.user.controller.UserController;
import com.example.dr_aids.user.domain.Role;
import com.example.dr_aids.user.domain.User;
import com.example.dr_aids.user.domain.UserUpdateDTO;
import com.example.dr_aids.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUpSecurityContext() {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.valueOf("DOCTOR"));
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        customUserDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("GET /user - 사용자 정보 조회 성공")
    void testUserInfoSuccess() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("testuser"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    @DisplayName("PUT /user - 사용자 정보 수정 성공")
    void testUpdateUserSuccess() throws Exception {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("newname");
        dto.setRole("newemail@example.com");

        doNothing().when(userService).updateUser(Mockito.any(), Mockito.any());

        mockMvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /user - 인증 정보 없이 접근할 때 400 반환")
    void testUserInfoFailWithoutAuth() throws Exception {
        SecurityContextHolder.clearContext(); // 인증 정보 제거

        mockMvc.perform(get("/user"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User details not found"));
    }
}
