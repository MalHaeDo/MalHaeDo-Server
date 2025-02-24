package com.backend.malhaedo.domain.member.service;

import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;
import com.backend.malhaedo.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    MemberResponseDTO.LoginSuccessDTO joinGuest(Member member, HttpServletResponse response);
//    MemberResponseDTO.LoginSuccessDTO login(Long memberId, HttpServletResponse response);
}
