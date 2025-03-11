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
import com.backend.malhaedo.global.prompt.dto.ClovaReply;
import com.backend.malhaedo.global.prompt.dto.ClovaResponse;
import com.backend.malhaedo.global.prompt.dto.SummaryReply;
import com.backend.malhaedo.global.prompt.dto.SummaryResponse;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final LetterRepository letterRepository;
    private final RecommendRepository recommendRepository;
    private final WebClient webClient;

    @Value("${clova.api.reply-url}")
    private String clovaApiUrl;

    @Value("${clova.api.key}")
    private String clovaApiKey;

    @Value("${clova.api.summary-url}")
    private String SummaryApiUrl;

    @Override
    public ReplyResponseDTO.ReplyResultDTO createReply(Member member, Long letterId) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LETTER_NOT_FOUND));

        replyRepository.findByLetter(letter).ifPresent(existingReply -> {
            throw new GeneralException(ErrorStatus.REPLY_EXIST);
        });

        ClovaReply replyContent = fetchReply(letter.getContent());
        SummaryReply summaryReply = createComment(replyContent.getContent());

        Reply reply = Reply.builder()
                .content(replyContent.getContent())
                .sender(replyContent.getSender())
                .summary(summaryReply.getSummary())
                .letter(letter)
                .build();

        Reply savedReply = replyRepository.save(reply);
        letter.increaseRepliedCount();

        return ReplyResponseDTO.ReplyResultDTO.builder()
                .replyId(savedReply.getReplyId())
                .content(savedReply.getContent())
                .sender(savedReply.getSender())
                .build();
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

        recommendRepository.deleteByLetter(letter);
        replyRepository.delete(reply);

        letter.decreaseRepliedCount();
    }

    private ClovaReply fetchReply(String content) {

        String requestBody = """
            {
              "messages": [
                {
                  "role": "system",
                  "content": "사용자가 작성한 부정적인 편지 내용에 따라 랜덤으로 주민 한명의 이름과 답문을 보내주세요. 주민들은 다람이, 펭글이, 뺍두리로 3명이 있습니다. 다람이는 쿨한 성격으로 말끝마다 ‘후후'를 붙입니다. 펭글이는 유쾌하고 발랄한 성격으로 말끝마다 ‘헤헷'을 붙입니다. 뱁뚜리는 겁이많고 소심한 성격으로 말끝마다 ‘...’을 붙입니다. 편지 내용에는 자신들의 유사경험담이 들어갑니다. 실제로 비슷한 상황을 겪었고, 그래서 어떻게 대처했는지 구체적으로 해결책을 제시해주고 공감을 해줍니다. 마지막으로 어떻게해서 부정적인 상황을 극복하고 긍정적인 마인드로 바꿀 수 있었는지 알려주면서 이러한 방법을 권유해주세요. 답문 길이는 최소 500자 이상이어야 합니다. 주민은 반드시 랜덤하게 선택해 주세요. 제일 처음엔 반드시 주민의 이름을 제시해야 합니다."
                },
                {
                  "role": "user",
                  "content": "%s"
                }
              ],
              "topP": 0.8,
              "topK": 0,
              "maxTokens": 1000,
              "temperature": 0.48,
              "repeatPenalty": 5.0,
              "stopBefore": [],
              "includeAiFilters": true,
              "seed": 2000
            }
            """.formatted(content);

        ClovaResponse response = webClient.post()
                .uri(clovaApiUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + clovaApiKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ClovaResponse.class)
                .block();

        if (response == null || response.getResult() == null || response.getResult().getMessage() == null) {
            throw new GeneralException(ErrorStatus.CLVOA_API_ERROR);
        }

        String fullContent = response.getResult().getMessage().getContent();
        log.info(fullContent);

        Resident sender = determineResident(fullContent);
        String contentWithoutResident = removeResidentName(fullContent);

        return new ClovaReply(sender, contentWithoutResident);
    }

    private String removeResidentName(String content) {
        return content.replaceFirst("^(다람이|펭글이|뺍뚜리)\\s*:", "").trim();
    }

    private Resident determineResident(String replyContent) {
        if (replyContent.startsWith("다람이")) {
            return Resident.DARAMI;
        } else if (replyContent.startsWith("펭글이")) {
            return Resident.PENGLE;
        } else if (replyContent.startsWith("뺍뚜리")) {
            return Resident.BAEBDURI;
        }
        return Resident.BAEBDURI; // 기본값
    }

    private SummaryReply createComment(String content) {

        String requestBody = """
            {
              "messages": [
                {
                  "role": "system",
                  "content": "주민의 답문을 한 줄로 요약해 주세요. 존댓말을 사용하지 마세요. 모든 답문은 조언으로 이루어져 있으므로 뒤에 조언을 해주고 있다는 말은 제외하세요."
                },
                {
                  "role": "user",
                  "content": "%s"
                }
              ],
              "topP": 0.8,
              "topK": 0,
              "maxTokens": 64,
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
        log.info(summary);

        return new SummaryReply(summary);
    }

}
