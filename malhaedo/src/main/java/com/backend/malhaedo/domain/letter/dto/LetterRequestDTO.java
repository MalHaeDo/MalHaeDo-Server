package com.backend.malhaedo.domain.letter.dto;

import lombok.Getter;

public class LetterRequestDTO {

    @Getter
    public static class LetterRequest {

        private String content;
        private Boolean isReplyAllowed;
    }
}
