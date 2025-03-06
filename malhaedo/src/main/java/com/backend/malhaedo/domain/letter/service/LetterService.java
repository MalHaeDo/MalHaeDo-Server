package com.backend.malhaedo.domain.letter.service;

import com.backend.malhaedo.domain.letter.dto.LetterRequestDTO;
import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.member.entity.Member;

public interface LetterService {

    Letter createLetter(LetterRequestDTO.LetterRequest request, Member member);
}
