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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final RecommendRepository recommendRepository;
    private final ReplyRepository replyRepository;

    @Override
    public RecommendResponseDTO.RecommendResultDTO createSongRecommend(Member member, Long replyId) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.REPLY_NOT_FOUND));

        // TODO: 클로바 api 연결 및 노래 추천
        String reason = "임의로 생성한 이유입니다.";
        String url = "임의로 생성한 노래 url 입니다.";
        String singer = "임의로 생성한 가수입니다.";
        String title = "임의로 생성한 노래 제목입니다.";

        Song song = Song.builder()
                .reason(reason)
                .url(url)
                .singer(singer)
                .title(title)
                .reply(reply)
                .build();

        Song savedSong = recommendRepository.save(song);

        return RecommendConverter.recommendResultDTO(savedSong);
    }
}
