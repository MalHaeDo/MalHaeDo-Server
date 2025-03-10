package com.backend.malhaedo.domain.letter.converter;

import com.backend.malhaedo.domain.letter.dto.LetterRequestDTO;
import com.backend.malhaedo.domain.letter.dto.LetterResponse;
import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.member.entity.Member;

public class LetterConverter {

    public static Letter toLetter(LetterRequestDTO.LetterRequest request, Member member, String summary) {
        return Letter.builder()
                .content(request.getContent())
                .isReplyAllowed(request.getIsReplyAllowed())
                .summary(summary)
                .member(member)
                .build();
    }

    public static LetterResponse.LetterResponseDTO toLetterResponseDTO(Letter letter) {
        return LetterResponse.LetterResponseDTO.builder()
                .letterId(letter.getLetterId())
                .build();
    }
}
