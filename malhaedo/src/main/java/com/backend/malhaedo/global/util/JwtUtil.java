package com.backend.malhaedo.global.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.member.repository.MemberRepository;
import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import com.backend.malhaedo.global.token.repository.RefreshTokenRepository;
import com.backend.malhaedo.global.error.exception.GeneralException;
import com.backend.malhaedo.global.token.entity.RefreshToken;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    private final MemberRepository memberRepository;

    private SecretKey secretKey;
    private long accessTokenExpirationTime;
    private long refreshTokenExpirationTime;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtUtil(MemberRepository memberRepository, @Value("${Jwt.secret}") String secret,
                   @Value("${Jwt.access-token.expiration-time}") long accessExpiration,
                   @Value("${Jwt.refresh-token.expiration-time}") long refreshExpiration,
                   RefreshTokenRepository refreshTokenRepository) {
        this.memberRepository = memberRepository;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationTime = accessExpiration; // Access 토큰 만료 시간 설정
        this.refreshTokenExpirationTime = refreshExpiration; // Refresh 토큰 만료 시간 설정
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 액세스 토큰을 발급하는 메서드
    public String generateAccessToken(Long memberId) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(secretKey)
                .compact();
    }

    @Transactional
    // 리프레쉬 토큰을 발급하는 메서드
    public String generateRefreshToken(Long memberId) {
        // 기존 리프레시 토큰 삭제
        refreshTokenRepository.deleteById(memberId);

        // 새 리프레시 토큰 발급 후 저장
        String refreshToken = Jwts.builder()
                .claim("memberId", memberId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpirationTime))
                .signWith(secretKey)
                .compact();
        refreshTokenRepository.save(new RefreshToken(memberId, refreshToken));
        return refreshToken;
    }

    // 응답 헤더에서 액세스 토큰을 반환하는 메서드
    public String getAccessTokenFromHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.split(" ")[1];
    }

    // 토큰에서 유저 id를 반환하는 메서드
    public Long getIdFromToken(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload()
                    .get("memberId", Long.class);
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우
            throw new GeneralException(ErrorStatus.INVALID_TOKEN);
        }
    }

    // 토큰에서 멤버를 반환하는 메서드
    public Member getMemberFromHeader(String authorizationHeader) { // annotation 추가 시 이용
        String token = getAccessTokenFromHeader(authorizationHeader);
        Long id = getIdFromToken(token);

        return memberRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    // Jwt 토큰의 유효기간을 확인하는 메서드
    public boolean isTokenExpired(String token) {
        try {
            Date expirationDate = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return expirationDate.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우
            throw new GeneralException(ErrorStatus.EXPIRED_TOKEN);
        }
    }
}
