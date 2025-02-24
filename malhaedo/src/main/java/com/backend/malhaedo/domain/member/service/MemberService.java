package com.backend.malhaedo.domain.member.service;

import com.backend.malhaedo.domain.member.dto.MemberRequestDTO;
import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;
import com.backend.malhaedo.domain.member.entity.Member;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    MemberResponseDTO.LoginSuccessDTO joinGuest(Member member, HttpServletResponse response);
//    MemberResponseDTO.LoginSuccessDTO login(Long memberId, HttpServletResponse response);

    Member setProfile(MemberRequestDTO.UpdateProfileRequestDTO request, Member member);
    void logout(Member member, HttpServletResponse response);
    void deleteMember(Member member);
}
