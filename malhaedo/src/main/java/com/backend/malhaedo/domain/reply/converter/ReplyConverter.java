package com.backend.malhaedo.domain.reply.converter;

import com.backend.malhaedo.domain.letter.entity.Letter;
import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;
import com.backend.malhaedo.domain.reply.entity.Reply;

import java.util.List;

public class ReplyConverter {

    public static ReplyResponseDTO.ReplyResultDTO replyResultDTO(Reply reply) {
        return ReplyResponseDTO.ReplyResultDTO.builder()
                .replyId(reply.getReplyId())
                .content(reply.getContent())
                .sender(reply.getSender())
                .build();
    }

    public static ReplyResponseDTO.ReplyPreViewDTO replyPreViewDTO(Reply reply) {
        return ReplyResponseDTO.ReplyPreViewDTO.builder()
                .replyId(reply.getReplyId())
                .summary(reply.getSummary())
                .sender(reply.getSender())
                .title(reply.getSong().getTitle())
                .singer(reply.getSong().getSinger())
                .build();
    }

    public static ReplyResponseDTO.ReplyPreViewListDTO replyPreVieWListDTO(List<Reply> replyList) {
        List<ReplyResponseDTO.ReplyPreViewDTO> replyPreViewDTOList = replyList.stream()
                .map(ReplyConverter::replyPreViewDTO)
                .toList();

        return ReplyResponseDTO.ReplyPreViewListDTO.builder()
                .replyList(replyPreViewDTOList)
                .totalElements(replyList.size())
                .build();
    }
}
