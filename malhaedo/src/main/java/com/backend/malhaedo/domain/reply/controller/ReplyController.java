package com.backend.malhaedo.domain.reply.controller;

import com.backend.malhaedo.domain.reply.dto.ReplyResponseDTO;
import com.backend.malhaedo.domain.reply.service.ReplyService;
import com.backend.malhaedo.global.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/reply")
public class ReplyController {

    private ReplyService replyService;

    @GetMapping("/{letterId}")
    @Operation(summary = "주민 답장 확인 API", description = "주민의 답장을 확인하고 저장하는 API 입니다. <br />"
            + "BAEBDURI(\"뱁뚜리\"), <br />" +
            "    DARAMI(\"다람이\"), <br />" +
            "    PENGLE(\"펭글이\"), <br />" +
            "    GOMDOONGI(\"곰둥이\")")
    public ApiResponse<ReplyResponseDTO.ReplyResultDTO> createReply(@PathVariable("letterId") Long letterId) {
        ReplyResponseDTO.ReplyResultDTO response = replyService.createReply();
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{replyId}/delete")
    @Operation(summary = "답장 삭제 API", description = "주민의 답장을 삭제하는 API 입니다.")
    public ApiResponse<Void> deleteReply(@PathVariable("replyId") Long replyId) {
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/storage")
    @Operation(summary = "유리병의 개수 확인 API", description = "유리병의 개수를 확인하는 API 입니다. <br />"
            + "보낸 편지 개수와 받은 답장 개수를 확인하세요. <br />"
            + "sentCount: 보낸 편지 개수, repliedCount: 답장 개수")
    public ApiResponse<ReplyResponseDTO.StorageListDTO> getStorageList() {
        ReplyResponseDTO.StorageListDTO response = replyService.getStorageList();
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/list")
    @Operation(summary = "답장 목록 확인 API", description = "주민의 답장 목록을 확인하는 API 입니다. <br />"
            + "답장 목록을 확인하세요.")
    public ApiResponse<ReplyResponseDTO.ReplyPreViewListDTO> getReplyList() {
        ReplyResponseDTO.ReplyPreViewListDTO response = replyService.getReplyList();
        return ApiResponse.onSuccess(response);
    }
}
