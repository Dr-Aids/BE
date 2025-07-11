package com.example.dr_aids.user.domain;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String email;
    private String loginType;
}

