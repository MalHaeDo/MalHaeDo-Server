package com.backend.malhaedo.global.prompt.service;

import com.backend.malhaedo.global.prompt.dto.PromptRequestDTO;
import com.backend.malhaedo.global.prompt.dto.PromptResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final WebClient webClient;

    @Value("${clova.api.url}")
    private String apiUrl;

    @Value("${clova.api.key}")
    private String apiKey;

//    public Mono<PromptResponseDTO> createTuningTask(PromptRequestDTO.ReplyPromptRequestDTO requestDto, String requestId) {
//        return webClient.post()
//                .uri(apiUrl + "/tuning/v2/tasks")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + apiKey)
////                .header("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId)
//                .bodyValue(requestDto)
//                .retrieve()
//                .bodyToMono(PromptResponseDTO.class);
//    }

//    public Mono<PromptResponseDTO> createTuningTask(PromptRequestDTO.ReplyPromptRequestDTO requestDto) {
//        return webClient.post()
//                .uri(apiUrl + "/tuning/v2/tasks")
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", "Bearer " + apiKey)
////                .header("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId)
//                .bodyValue(requestDto)
//                .retrieve()
//                .bodyToMono(PromptResponseDTO.class);
//    }

    public Mono<PromptResponseDTO> createTuningTask(PromptRequestDTO.ReplyPromptRequestDTO requestDto) {
        return webClient.post()
                .uri(apiUrl + "/tuning/v2/tasks")
                .header("Authorization", "Bearer " + apiKey) // ✅ API Key 추가
//                .header("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId)
                .header("Content-Type", "application/json") // ✅ Content-Type 설정
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(PromptResponseDTO.class)
                .doOnError(error -> System.err.println("❌ [에러] Clova API 호출 실패: " + error.getMessage()));
    }
}
