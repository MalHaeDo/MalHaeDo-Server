package com.backend.malhaedo.domain.reply.service;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.letter.repository.LetterRepository;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.recommend.repository.RecommendRepository;
import com.backend.malhaedo.domain.reply.converter.ReplyConverter;
import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;
import com.backend.malhaedo.domain.reply.entity.Reply;
import com.backend.malhaedo.domain.reply.repository.ReplyRepository;
import com.backend.malhaedo.global.common.enums.Resident;
import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import com.backend.malhaedo.global.error.exception.GeneralException;
import com.backend.malhaedo.global.prompt.dto.PromptRequestDTO;
import com.backend.malhaedo.global.prompt.dto.PromptResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final LetterRepository letterRepository;
    private final RecommendRepository recommendRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${clova.api.url}")
    private String clovaApiUrl;

    @Value("${clova.api.key}")
    private String clovaApiKey;

    @Value("${clova.api.access-key}")
    private String accessKey;

    @Value("${clova.api.secret-key}")
    private String secretKey;

    @Override
    public ReplyResponseDTO.ReplyResultDTO createReply(Member member, Long letterId) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LETTER_NOT_FOUND));

        String content = getReplyFromClova(buildRequestDTO(letter.getContent())); // TODO: 클로바 api 연결

        Reply reply = Reply.builder()
                .content(content)
                .sender(Resident.BAEBDURI) // TODO: 주민 선택
                .letter(letter)
                .build();

        Reply savedReply = replyRepository.save(reply);
        letter.increaseRepliedCount();

        return ReplyConverter.replyResultDTO(savedReply);
    }

    private PromptRequestDTO.ReplyPromptRequestDTO buildRequestDTO(String letterContent) {
        return new PromptRequestDTO.ReplyPromptRequestDTO(
                "MyTuningTask",
                "HCX-003-base",
                "PEFT",
                "GENERATION",
                8,
                "1.0E-4",
                "reply_dataset.json",
                "my-bucket",
                accessKey,
                secretKey
        );
    }

    private String getReplyFromClova(PromptRequestDTO.ReplyPromptRequestDTO requestDTO) {
        try {
            // ✅ DTO를 JSON String으로 변환
            String requestBody = objectMapper.writeValueAsString(requestDTO);
            System.out.println("📌 [Clova API 요청] Body: " + requestBody);

            PromptResponseDTO response = webClient.post()
                    .uri(clovaApiUrl + "/tuning/v2/tasks")
                    .header("Authorization", "Bearer " + clovaApiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody) // ✅ JSON String을 Body로 전송
                    .retrieve()
                    .bodyToMono(PromptResponseDTO.class)
                    .doOnError(error -> System.err.println("❌ [Clova API 호출 실패] " + error.getMessage()))
                    .block();

            if (response == null || response.getResult() == null) {
                throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
            }

            return response.getResult().getName();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeneralException(ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Reply> getReplyList(Member member) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        List<Reply> replyList = replyRepository.findAllByLetter_Member_MemberId(member.getMemberId());

        return replyList;
    }

    @Override
    public ReplyResponseDTO.StorageListDTO getStorageList(Member member) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        List<Letter> letters = letterRepository.findAllByMember_MemberId(member.getMemberId());

        int sentCount = letters.size();
        int repliedCount = letters.stream().mapToInt(Letter::getRepliedCount).sum();

        return ReplyConverter.toStorageListDTO(sentCount, repliedCount);
    }

    @Override
    public void deleteReply(Member member, Long replyId) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REPLY_NOT_FOUND));

        Letter letter = reply.getLetter();
        if (!letter.getMember().getMemberId().equals(member.getMemberId())) {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED_ACCESS);
        }

        recommendRepository.deleteByReply(reply);
        replyRepository.delete(reply);

        letter.decreaseRepliedCount();
    }

    @PostConstruct
    public void init() {
        PromptRequestDTO.ReplyPromptRequestDTO testDto = new PromptRequestDTO.ReplyPromptRequestDTO(
                "MyTuningTask",
                "clova-base",
                "fine-tuning",
                "text-classification",
                5,
                "0.001",
                "s3://my-bucket/dataset.json",
                "my-bucket",
                accessKey,
                secretKey
        );

        try {
            String json = objectMapper.writeValueAsString(testDto);
            System.out.println("📌 [테스트 JSON 변환] " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
