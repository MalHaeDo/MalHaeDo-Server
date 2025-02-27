package com.backend.malhaedo.domain.recommend.service;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.letter.repository.LetterRepository;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.recommend.converter.RecommendConverter;
import com.backend.malhaedo.domain.recommend.dto.RecommendResponseDTO;
import com.backend.malhaedo.domain.recommend.entity.Song;
import com.backend.malhaedo.domain.recommend.repository.RecommendRepository;
import com.backend.malhaedo.domain.reply.entity.Reply;
import com.backend.malhaedo.domain.reply.repository.ReplyRepository;
import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import com.backend.malhaedo.global.error.exception.GeneralException;
import com.backend.malhaedo.global.prompt.dto.ClovaResponse;
import com.backend.malhaedo.global.prompt.dto.ClovaSong;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;
    private final ReplyRepository replyRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${clova.api.song-url}")
    private String clovaApiUrl;

    @Value("${clova.api.key}")
    private String clovaApiKey;

    @Override
    public RecommendResponseDTO.RecommendResultDTO createSongRecommend(Member member, Long replyId) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REPLY_NOT_FOUND));

        // TODO: 클로바 api 연결 및 노래 추천
        ClovaSong clovaSong = fetchSongRecommendationFromClova(reply.getLetter().getContent());

        Song song = Song.builder()
                .reason(clovaSong.getReason()) // 노래 추천 이유 저장
                .singer(clovaSong.getSinger()) // 가수 저장
                .title(clovaSong.getTitle())   // 노래 제목 저장
                .reply(reply)
                .build();

        Song savedSong = recommendRepository.save(song);

        return RecommendConverter.recommendResultDTO(savedSong);
    }

    private String generateSongRequestBody(String letterContent) {
        return """
        {
          "messages": [
            {
              "role": "system",
              "content": "사용자가 작성한 편지 내용에 따라 이장님이 노래를 추천해주세요. 사용자 편지 내용에 적힌 키워드를 통해 상황을 분석해서 공감 및 위로해주거나 기분 전환시켜줄 수 있는 국내 노래로 42분 이내 유튜브 영상을 보여주세요. 더불어 이장님이 섬과 바다, 바람, 나무 등 자연에 빗대어 왜 이 노래를 추천하는지 설명해주세요. 참고로 이장님 성격은 느긋하며 말투는 하오체를 쓰고 말끝마다 ‘뚜벅’을 붙입니다. 편지의 마지막엔 노래 제목, 가수 이름을 적어주세요. 다음은 이장님의 답변 예시입니다.\\n\\n음, 삶이 힘들고 지치는구려. 일도, 사람도 마음도 무거운 걸 보니, 마치 거센 파도가 치는 바닷가에 홀로 서 있는 기분이겠구려. 그대가 지금 마주한 바람은 매섭고 차갑지만, 바다는 늘 그렇듯 한결같이 출렁이며 언젠가 잔잔해질 것이오. 이럴 땐 조용히 파도를 바라보며 잠시 쉬어가는 것이 좋겠구려. 그러니 이 노래를 들으며 잠깐 마음을 내려놓아 보시게. 가만히 바닷가에 앉아 파도 소리를 듣는 기분으로, 뚜벅.\\n\\n노래 제목: 바람이 불어오는 곳\\n가수: 김광석"
            },
            {
              "role": "user",
              "content": "%s"
            }
          ],
          "topP": 0.8,
          "topK": 0,
          "maxTokens": 1014,
          "temperature": 0.48,
          "repeatPenalty": 5.0,
          "stopBefore": [],
          "includeAiFilters": true,
          "seed": 0
        }
        """.formatted(letterContent);
    }


    private ClovaSong fetchSongRecommendationFromClova(String letterContent) {

        // API 요청
        ClovaResponse response = webClient.post()
                .uri(clovaApiUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + clovaApiKey)
                .bodyValue(generateSongRequestBody(letterContent))
                .retrieve()
                .bodyToMono(ClovaResponse.class)
                .block();

        if (response == null || response.getResult() == null || response.getResult().getMessage() == null) {
            throw new GeneralException(ErrorStatus.CLVOA_API_ERROR);
        }

        String fullContent = response.getResult().getMessage().getContent();

        // ✨ 노래 제목과 가수 추출
        String songTitle = extractSongTitle(fullContent);
        String songSinger = extractSongSinger(fullContent);

        // ✨ 추천 이유에서 노래 정보 제거
        String reasonWithoutTitle = removeSongInfo(fullContent);
        String cleanedReason = cleanReasonText(reasonWithoutTitle);

        return new ClovaSong(cleanedReason, songTitle, songSinger);
    }

    private String extractSongTitle(String content) {
        Pattern pattern = Pattern.compile("노래 제목: (.+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "알 수 없는 제목"; // 기본값
    }

    private String extractSongSinger(String content) {
        Pattern pattern = Pattern.compile("가수: (.+)");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "알 수 없는 가수"; // 기본값
    }

    private String removeSongInfo(String content) {
        return content.replaceAll("노래 제목: .+\\n가수: .+", "").trim();
    }

    private String cleanReasonText(String reason) {
        if (reason == null) return "";

        // 개행 문자를 OS에 맞게 변환
        return reason.replaceAll("\\n", System.lineSeparator()).trim();
    }

}
