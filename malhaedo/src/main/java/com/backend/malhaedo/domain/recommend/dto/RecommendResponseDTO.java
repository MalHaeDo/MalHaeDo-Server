package com.backend.malhaedo.domain.recommend.dto;

import com.backend.malhaedo.global.common.enums.Resident;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecommendResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendResultDTO {
        private Long songId;
        private String reason;
        private String url;
        private String singer;
        private String title;
    }
}
