package com.backend.malhaedo.domain.recommend.repository;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.recommend.entity.Song;
import com.backend.malhaedo.domain.reply.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Song, Long> {
    void deleteByLetter(Letter letter);
}
