package com.backend.malhaedo.domain.recommend.service;

import com.backend.malhaedo.domain.recommend.dto.RecommendResponseDTO;

public interface RecommendService {

    RecommendResponseDTO.RecommendResultDTO createSongRecommend();
}
