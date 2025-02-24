package com.backend.malhaedo.domain.letter.controller;

import com.backend.malhaedo.domain.letter.converter.LetterConverter;
import com.backend.malhaedo.domain.letter.dto.LetterRequestDTO;
import com.backend.malhaedo.domain.letter.dto.LetterResponse;
import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.letter.service.LetterService;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.global.annotation.CurrentMember;
import com.backend.malhaedo.global.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/letter")
public class LetterController {

    private final LetterService letterService;

    @PostMapping("/send")
    @Operation(summary = "편지 보내기 API", description = "편지를 보내는 API 입니다. <br />"
            + "편지 내용을 입력하세요. <br />"
            + "답장을 허용할지 여부를 선택하세요. <br />")
    public ApiResponse<LetterResponse.LetterResponseDTO> sendLetter(
            @CurrentMember Member member, @RequestBody LetterRequestDTO.LetterRequest request) {
        Letter letter = letterService.createLetter(request, member);
        return ApiResponse.onSuccess(LetterConverter.toLetterResponseDTO(letter));
    }
}
