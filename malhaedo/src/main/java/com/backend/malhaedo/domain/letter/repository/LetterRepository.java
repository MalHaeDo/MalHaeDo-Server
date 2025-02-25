package com.backend.malhaedo.domain.letter.repository;

import com.backend.malhaedo.domain.letter.entity.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LetterRepository extends JpaRepository<Letter, Long> {

    Optional<Letter> findByLetterId(Long letterId);
    List<Letter> findAllByMember_MemberId(Long memberId);
    Letter findByMember_MemberId(Long memberId);
}
