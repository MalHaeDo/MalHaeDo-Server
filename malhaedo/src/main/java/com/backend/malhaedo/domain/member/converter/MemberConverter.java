package com.backend.malhaedo.domain.member.converter;

import com.backend.malhaedo.domain.member.entity.Member;

public class MemberConverter {

    public static Member toSocialMember(String provider, String providerId) {

        return Member.builder()
                .isGuest(false)
                .provider(provider)
                .providerId(providerId)
                .build();
    }
}
