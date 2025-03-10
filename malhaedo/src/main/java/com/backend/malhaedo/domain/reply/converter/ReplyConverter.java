package com.backend.malhaedo.domain.reply.converter;

import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;
import com.backend.malhaedo.domain.reply.entity.Reply;

import java.util.List;

public class ReplyConverter {

    public static ReplyResponseDTO.ReplyPreViewDTO replyPreViewDTO(Reply reply) {
        return ReplyResponseDTO.ReplyPreViewDTO.builder()
                .replyId(reply.getReplyId())
                .replySummary(reply.getSummary())
                .letterSummary(reply.getLetter().getSummary())
                .sender(reply.getSender())
                .title(reply.getLetter().getSong().getTitle())
                .singer(reply.getLetter().getSong().getSinger())
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

    public static ReplyResponseDTO.StorageListDTO toStorageListDTO(int sentCount, int repliedCount) {
        return ReplyResponseDTO.StorageListDTO.builder()
                .sentCount(sentCount)
                .repliedCount(repliedCount)
                .build();
    }

}
