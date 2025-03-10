package com.backend.malhaedo.domain.recommend.service;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.letter.repository.LetterRepository;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.recommend.converter.RecommendConverter;
import com.backend.malhaedo.domain.recommend.dto.RecommendResponseDTO;
import com.backend.malhaedo.domain.recommend.entity.Song;
import com.backend.malhaedo.domain.recommend.repository.RecommendRepository;
import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import com.backend.malhaedo.global.error.exception.GeneralException;
import com.backend.malhaedo.global.prompt.dto.ClovaResponse;
import com.backend.malhaedo.global.prompt.dto.ClovaSong;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;
    private final LetterRepository letterRepository;
    private final WebClient webClient;

    @Value("${clova.api.song-url}")
    private String clovaApiUrl;

    @Value("${clova.api.key}")
    private String clovaApiKey;

    @Value("${youtube.api.key}")
    private String youtubeApiKey;

    @Override
    public RecommendResponseDTO.RecommendResultDTO createSongRecommend(Member member, Long letterId) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LETTER_NOT_FOUND));

        ClovaSong clovaSong = fetchSongRecommend(letter.getContent());
        String videoUrl;

        try {
            videoUrl = searchVideo(clovaSong.getSinger() + " " + clovaSong.getTitle());
        } catch (IOException e) {
            videoUrl = "URL 검색 실패"; // 검색 실패 시 기본값
        }

        Song song = Song.builder()
                .letter(letter)
                .reason(clovaSong.getReason()) // 추천 이유
                .singer(clovaSong.getSinger()) // 가수
                .title(clovaSong.getTitle())   // 노래 제목
                .url(videoUrl) // 유튜브 URL
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
              "content": "사용자가 작성한 편지 내용에 따라 이장님이 노래를 추천해주세요. 사용자 편지 내용에 적힌 키워드를 통해 상황을 분석해서 공감 및 위로해주거나 기분 전환시켜줄 수 있는 국내 노래를 추천해 주세요. 노래는 네이버뮤직에서 찾아주세요. 더불어 이장님이 섬과 바다, 바람, 나무 등 자연에 빗대어 왜 이 노래를 추천하는지 500 글자 이상 설명해주세요. 참고로 이장님 성격은 느긋하며 말투는 하게체를 쓰고 말끝마다 ‘뚜벅'을 붙입니다. 상대방을 지칭하는 칭호는 '자네'입니다. 편지의 마지막엔 노래 제목, 가수 이름을 적어주세요.\s 다음은 이장님의 답변 예시입니다.\\n\\n음, 삶이 힘들고 지치는구만. 일도, 사람도 마음도 무거운 걸 보니, 마치 거센 파도가 치는 바닷가에 홀로 서 있는 기분이겠구만. 그대가 지금 마주한 바람은 매섭고 차갑지만, 바다는 늘 그렇듯 한결같이 출렁이며 언젠가 잔잔해질 것이네. 이럴 땐 조용히 파도를 바라보며 잠시 쉬어가는 것이 좋겠구만. 그러니 이 노래를 들으며 잠깐 마음을 내려놓아 보시게. 가만히 바닷가에 앉아 파도 소리를 듣는 기분으로, 뚜벅.\\n\\n노래 제목: 바람이 불어오는 곳\\n가수: 김광석"
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


    private ClovaSong fetchSongRecommend(String letterContent) {

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

        String songTitle = extractSongTitle(fullContent);
        String songSinger = extractSongSinger(fullContent);

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

        return reason.replaceAll("\\n", System.lineSeparator()).trim();
    }

    public String searchVideo(String query) throws IOException {
        // JSON 데이터를 처리하기 위한 JsonFactory 객체 생성
        JsonFactory jsonFactory = new JacksonFactory();

        // YouTube 객체를 빌드하여 API에 접근할 수 있는 YouTube 클라이언트 생성
        YouTube youtube = new YouTube.Builder(
                new com.google.api.client.http.javanet.NetHttpTransport(),
                jsonFactory,
                request -> {})
                .build();

        // YouTube Search API를 사용하여 동영상 검색을 위한 요청 객체 생성
        YouTube.Search.List search = youtube.search().list(Collections.singletonList("id,snippet"));

        // API 키 설정
        search.setKey(youtubeApiKey);

        // 검색어 설정
        search.setQ(query);

        // 동영상만 검색
        search.setType(Collections.singletonList("video"));

        // 검색 요청 실행 및 응답 받아오기
        SearchListResponse searchResponse = search.execute();

        // 검색 결과에서 동영상 목록 가져오기
        List<SearchResult> searchResultList = searchResponse.getItems();

        if (searchResultList != null && !searchResultList.isEmpty()) {
            SearchResult searchResult = searchResultList.get(0);
            String videoId = searchResult.getId().getVideoId();
            return "https://www.youtube.com/watch?v=" + videoId;
        }

        return "검색 결과가 없습니다";
    }

}
