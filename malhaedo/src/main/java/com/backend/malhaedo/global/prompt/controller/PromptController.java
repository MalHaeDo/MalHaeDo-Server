package com.backend.malhaedo.global.prompt.controller;

import com.backend.malhaedo.global.prompt.dto.PromptRequestDTO;
import com.backend.malhaedo.global.prompt.dto.PromptResponseDTO;
import com.backend.malhaedo.global.prompt.service.PromptService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/prompt")
public class PromptController {

    private final PromptService promptService;

    @PostMapping("/reply")
    @SecurityRequirement(name = "")
    public Mono<ResponseEntity<PromptResponseDTO>> createTuningTask(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestHeader(value = "X-NCP-CLOVASTUDIO-REQUEST-ID", required = false) String requestId,
            @RequestBody PromptRequestDTO.ReplyPromptRequestDTO requestDto
    ) {
//        return promptService.createTuningTask(requestDto, requestId)
//                .map(ResponseEntity::ok);
        System.out.println("📌 [로그] Authorization 헤더: " + authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Mono.error(new RuntimeException("❌ Authorization 헤더가 누락되었거나 잘못되었습니다."));
        }

        return promptService.createTuningTask(requestDto)
                .map(ResponseEntity::ok);
    }
}
