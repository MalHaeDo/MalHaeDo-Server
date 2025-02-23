package com.backend.malhaedo.global.jwt.handler;

import java.io.IOException;

import com.backend.malhaedo.global.error.ApiResponse;
import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws
            IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(403);

        ApiResponse<Object> errorResponse = ApiResponse.onFailure(
                ErrorStatus.FORBIDDEN.getCode(),
                ErrorStatus.FORBIDDEN.getMessage(),
                null
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
