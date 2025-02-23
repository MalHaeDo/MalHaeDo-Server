package com.backend.malhaedo.global.token.service;

import com.backend.malhaedo.global.token.dto.TokenResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    TokenResponseDTO.TokenDTO reissueAccessToken(HttpServletRequest request, HttpServletResponse response);
}
