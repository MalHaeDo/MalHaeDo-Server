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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final LetterRepository letterRepository;
    private final RecommendRepository recommendRepository;

    @Override
    public ReplyResponseDTO.ReplyResultDTO createReply(Member member, Long letterId) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LETTER_NOT_FOUND));

        String content = "임의로 생성된 답장입니다."; // TODO: 클로바 api 연결

        Reply reply = Reply.builder()
                .content(content)
                .sender(Resident.BAEBDURI) // TODO: 주민 선택
                .letter(letter)
                .build();

        Reply savedReply = replyRepository.save(reply);
        letter.increaseRepliedCount();

        return ReplyConverter.replyResultDTO(savedReply);
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
}
