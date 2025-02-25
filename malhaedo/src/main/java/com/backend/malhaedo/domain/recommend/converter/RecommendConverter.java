package com.backend.malhaedo.domain.recommend.converter;

import com.backend.malhaedo.domain.recommend.dto.RecommendResponseDTO;
import com.backend.malhaedo.domain.recommend.entity.Song;

public class RecommendConverter {

    public static RecommendResponseDTO.RecommendResultDTO recommendResultDTO(Song song) {
        return RecommendResponseDTO.RecommendResultDTO.builder()
                .songId(song.getSongId())
                .reason(song.getReason())
                .title(song.getTitle())
                .singer(song.getSinger())
                .url(song.getUrl())
                .build();
    }
}
