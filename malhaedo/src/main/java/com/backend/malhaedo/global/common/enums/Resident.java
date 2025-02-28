package com.backend.malhaedo.global.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Resident {

    BAEBDURI("뱁뚜리"),
    DARAMI("다람이"),
    PENGLE("펭글이");

    private final String sender;
}
