package com.backend.malhaedo.global.prompt.dto;

import com.backend.malhaedo.global.common.enums.Resident;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClovaReply {
    private Resident sender;
    private String content;
}