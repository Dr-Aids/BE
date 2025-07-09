package com.example.dr_aids.user.domain;

public enum Role {
    DOCTOR("ROLE_DOCTOR"),
    NURSE("ROLE_NURSE");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
