package com.backend.malhaedo.domain.reply.dto;

import com.backend.malhaedo.global.common.enums.Resident;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReplyResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyResultDTO {
        private Long replyId;
        private Resident sender;
        private String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorageListDTO {
        private int sentCount;
        private int repliedCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyPreViewDTO {
        private Long replyId;
        private String summary;
        private Resident sender;
        private String title;
        private String singer;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReplyPreViewListDTO {
        List<ReplyPreViewDTO> replyList;
        int repliedCount;
    }
}
