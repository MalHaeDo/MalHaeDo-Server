package com.backend.malhaedo.domain.member.service;

import com.backend.malhaedo.domain.member.converter.MemberConverter;
import com.backend.malhaedo.domain.member.dto.MemberRequestDTO;
import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.member.repository.MemberRepository;
import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import com.backend.malhaedo.global.error.exception.GeneralException;
import com.backend.malhaedo.global.token.repository.RefreshTokenRepository;
import com.backend.malhaedo.global.util.CookieUtil;
import com.backend.malhaedo.global.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public MemberResponseDTO.LoginSuccessDTO joinGuest(Member member, HttpServletResponse response) {

        Member loginMember = memberRepository.save(member);

        String accessToken = jwtUtil.generateAccessToken(loginMember.getMemberId());
        setRefreshToken(loginMember.getMemberId(), response);
        return MemberConverter.toLoginSuccessDTO(member,accessToken);
    }

//    @Override
//    public MemberResponseDTO.LoginSuccessDTO login(Long memberId, HttpServletResponse response) {
//
//        Member loginMember = memberRepository.findById(memberId)
//                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
//
//        String accessToken = jwtUtil.generateAccessToken(loginMember.getMemberId());
//        setRefreshToken(loginMember.getMemberId(), response);
//        return MemberConverter.toLoginSuccessDTO(loginMember.getMemberId(),accessToken);
//    }

    private void setRefreshToken(Long memberId, HttpServletResponse response) {
        // 새 RefreshToken 발급
        String refreshToken = jwtUtil.generateRefreshToken(memberId);

        // response에 cookie로 반환
        Cookie cookie = cookieUtil.createCookie(refreshToken);
        response.addCookie(cookie);
    }

    @Override
    public Member setProfile(MemberRequestDTO.UpdateProfileRequestDTO request, Member member) {

        if (member == null) throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        member.setNickName(request.getNickName());
        member.setIslandName(request.getIslandName());

        return memberRepository.save(member);
    }

    @Override
    public void logout(Member member, HttpServletResponse response) {

        refreshTokenRepository.deleteById(member.getMemberId());

        Cookie deletedCookie = cookieUtil.deleteCookie();
        response.addCookie(deletedCookie);
    }

    @Override
    public void deleteMember(Member member) {

        refreshTokenRepository.deleteById(member.getMemberId());
        memberRepository.delete(member);
    }

}
