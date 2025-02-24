package com.backend.malhaedo.domain.reply.service;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;

public interface ReplyService {

    ReplyResponseDTO.ReplyResultDTO createReply(Member member, Long letterId);
    ReplyResponseDTO.StorageListDTO getStorageList();
    ReplyResponseDTO.ReplyPreViewListDTO getReplyList();
}
