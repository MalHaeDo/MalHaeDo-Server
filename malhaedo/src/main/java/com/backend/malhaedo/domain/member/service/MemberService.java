package com.backend.malhaedo.domain.member.service;

import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    MemberResponseDTO.LoginSuccessDTO joinMember();
    MemberResponseDTO.LoginSuccessDTO login(Long memberId, HttpServletResponse response);
}
