package com.backend.malhaedo.domain.reply.converter;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;
import com.backend.malhaedo.domain.reply.entity.Reply;

public class ReplyConverter {

    public static ReplyResponseDTO.ReplyResultDTO replyResultDTO(Reply reply) {
        return ReplyResponseDTO.ReplyResultDTO.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .sender(reply.getSender())
                .build();
    }
}
