package com.backend.malhaedo.domain.reply.service;

import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;

public interface ReplyService {

    ReplyResponseDTO.ReplyResultDTO createReply();
    ReplyResponseDTO.StorageListDTO getStorageList();
    ReplyResponseDTO.ReplyPreViewListDTO getReplyList();
}
