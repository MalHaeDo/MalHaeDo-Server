package com.backend.malhaedo.global.token.repository;

import java.util.Optional;

import com.backend.malhaedo.global.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findById(Long memberId);
    void deleteById(Long memberId);
}
