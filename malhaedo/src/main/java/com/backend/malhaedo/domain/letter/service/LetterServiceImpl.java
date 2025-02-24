package com.backend.malhaedo.domain.letter.service;

import com.backend.malhaedo.domain.letter.dto.LetterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

    @Override
    public LetterResponse.LetterResponseDTO createLetter() {
        return null;
    }
}
