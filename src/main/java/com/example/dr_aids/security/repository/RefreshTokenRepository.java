package com.example.dr_aids.security.repository;

import com.example.dr_aids.security.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    void deleteByRefresh(String refresh);

    Boolean existsByRefresh(String refresh);
}
