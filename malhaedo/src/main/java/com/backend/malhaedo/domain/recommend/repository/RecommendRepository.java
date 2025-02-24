package com.backend.malhaedo.domain.recommend.repository;

import com.backend.malhaedo.domain.recommend.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Song, Long> {
}
