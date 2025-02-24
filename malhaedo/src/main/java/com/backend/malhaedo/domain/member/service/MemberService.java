package com.backend.malhaedo.domain.member.service;

import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;

public interface MemberService {

    MemberResponseDTO.LoginSuccessDTO joinMember();
}
