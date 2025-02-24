package com.backend.malhaedo.domain.recommend.service;

import com.backend.malhaedo.domain.recommend.dto.RecommendResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    @Override
    public RecommendResponseDTO.RecommendResultDTO createSongRecommend() {
        return null;
    }
}
