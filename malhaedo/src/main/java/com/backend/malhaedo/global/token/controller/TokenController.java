package com.backend.malhaedo.global.token.controller;

import com.backend.malhaedo.global.error.ApiResponse;
import com.backend.malhaedo.global.token.dto.TokenResponseDTO;
import com.backend.malhaedo.global.token.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    // 액세스 토큰을 재발행하는 API
    @GetMapping("/reissue/access-token")
    public ApiResponse<TokenResponseDTO.TokenDTO> reissueAccessToken(HttpServletRequest request,
                                                                     HttpServletResponse response) {
        TokenResponseDTO.TokenDTO accessToken = tokenService.reissueAccessToken(request, response);
        return ApiResponse.onSuccess(accessToken);
    }
}
