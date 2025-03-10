package com.backend.malhaedo.domain.letter.service;

import com.backend.malhaedo.domain.letter.converter.LetterConverter;
import com.backend.malhaedo.domain.letter.dto.LetterRequestDTO;
import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.letter.repository.LetterRepository;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import com.backend.malhaedo.global.error.exception.GeneralException;
import com.backend.malhaedo.global.prompt.dto.ClovaResponse;
import com.backend.malhaedo.global.prompt.dto.SummaryReply;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
@RequiredArgsConstructor
public class LetterServiceImpl implements LetterService {

    private final LetterRepository letterRepository;
    private final WebClient webClient;

    @Value("${clova.api.key}")
    private String clovaApiKey;

    @Value("${clova.api.summary-url}")
    private String SummaryApiUrl;

    @Override
    public Letter createLetter(LetterRequestDTO.LetterRequest request, Member member) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        SummaryReply summaryReply = createSummary(request.getContent());
        Letter letter = LetterConverter.toLetter(request, member, summaryReply.getSummary());

        letter.increaseSentCount(); // 편지 보낸 횟수 증가

        return letterRepository.save(letter);
    }

    private SummaryReply createSummary(String content) {

        String requestBody = """
            {
              "messages": [
                {
                  "role": "system",
                  "content": "사용자가 보낸 편지의 내용을 반말로 요약해주세요. 편지의 내용이 20자 이하라면 그대로 반환하세요. 문장은 명사나 음슴체로 마무리해주세요."
                },
                {
                  "role": "user",
                  "content": "%s"
                }
              ],
              "topP": 0.8,
              "topK": 0,
              "maxTokens": 128,
              "temperature": 0.5,
              "repeatPenalty": 5.0,
              "stopBefore": [],
              "includeAiFilters": true,
              "seed": 0
            }
            """.formatted(content);

        ClovaResponse response = webClient.post()
                .uri(SummaryApiUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + clovaApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ClovaResponse.class)
                .block();


        if (response == null || response.getResult() == null || response.getResult().getMessage() == null) {
            throw new GeneralException(ErrorStatus.CLVOA_API_ERROR);
        }

        String summary = response.getResult().getMessage().getContent();

        return new SummaryReply(summary);
    }
}
