package com.backend.malhaedo.domain.member.dto;

import lombok.Getter;

public class MemberRequestDTO {

    @Getter
    public static class UpdateProfileRequestDTO {

        private String nickName;
        private String islandName;
    }
}
