package com.backend.malhaedo.domain.letter.repository;

import com.backend.malhaedo.domain.letter.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Optional<Letter> findByLetterId(Long letterId);
}
