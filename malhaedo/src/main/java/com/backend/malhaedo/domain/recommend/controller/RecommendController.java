package com.backend.malhaedo.domain.recommend.controller;

import com.backend.malhaedo.domain.recommend.dto.RecommendResponseDTO;
import com.backend.malhaedo.domain.recommend.service.RecommendService;
import com.backend.malhaedo.global.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/recommend")
public class RecommendController {

    private RecommendService recommendService;

    @GetMapping("/{letterId}")
    @Operation(summary = "이장님 답장 확인 API", description = "이장님의 답장을 확인하는 API 입니다.")
    public ApiResponse<RecommendResponseDTO.RecommendResultDTO> createRecommend(@PathVariable("letterId") Long letterId) {
        RecommendResponseDTO.RecommendResultDTO response = recommendService.createSongRecommend();
        return ApiResponse.onSuccess(response);
    }
}
