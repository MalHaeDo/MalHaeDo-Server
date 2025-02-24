package com.backend.malhaedo.domain.letter.repository;

import com.backend.malhaedo.domain.letter.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Long> {
}
