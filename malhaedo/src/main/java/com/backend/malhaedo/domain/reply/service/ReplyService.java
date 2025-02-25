package com.backend.malhaedo.domain.reply.service;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;
import com.backend.malhaedo.domain.reply.entity.Reply;

import java.util.List;

public interface ReplyService {

    ReplyResponseDTO.ReplyResultDTO createReply(Member member, Long letterId);
    List<Reply> getReplyList(Member member);
    ReplyResponseDTO.StorageListDTO getStorageList(Member member);
    void deleteReply(Member member, Long replyId);
}
