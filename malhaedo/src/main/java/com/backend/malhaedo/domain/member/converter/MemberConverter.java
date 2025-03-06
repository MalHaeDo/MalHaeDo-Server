package com.backend.malhaedo.domain.member.converter;

import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;
import com.backend.malhaedo.domain.member.entity.Member;

public class MemberConverter {

    public static Member toSocialMember(String provider, String providerId) {

        return Member.builder()
                .isGuest(false)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

    public static MemberResponseDTO.LoginSuccessDTO toLoginSuccessDTO(Member member, String accessToken) {
        return MemberResponseDTO.LoginSuccessDTO.builder()
                .accessToken(accessToken)
                .build();
    }

    public static Member toJoinGuest() {
        return Member.builder()
                .isGuest(true)
                .build();
    }

}
