package com.backend.malhaedo.domain.letter.service;

import com.backend.malhaedo.domain.letter.converter.LetterConverter;
import com.backend.malhaedo.domain.letter.dto.LetterRequestDTO;
import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.letter.repository.LetterRepository;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import com.backend.malhaedo.global.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

    private final LetterRepository letterRepository;

    @Override
    public Letter createLetter(LetterRequestDTO.LetterRequest request, Member member) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        Letter letter = LetterConverter.toLetter(request, member);

        letter.increaseSentCount(); // 편지 보낸 횟수 증가

        return letterRepository.save(letter);
    }
}
