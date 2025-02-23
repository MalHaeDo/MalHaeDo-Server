package com.backend.malhaedo.global.token.converter;


import com.backend.malhaedo.global.token.dto.TokenResponseDTO;

public class TokenConverter {
    public static TokenResponseDTO.TokenDTO toTokenDTO(String accessToken) {
        return TokenResponseDTO.TokenDTO.builder()
                .accessToken(accessToken)
                .build();
    }
}
