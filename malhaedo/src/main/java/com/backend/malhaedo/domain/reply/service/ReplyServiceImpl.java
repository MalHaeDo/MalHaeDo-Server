package com.backend.malhaedo.domain.reply.service;

import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    @Override
    public ReplyResponseDTO.ReplyResultDTO createReply() {
        return null;
    }

    @Override
    public ReplyResponseDTO.StorageListDTO getStorageList() {
        return null;
    }

    @Override
    public ReplyResponseDTO.ReplyPreViewListDTO getReplyList() {
        return null;
    }
}
