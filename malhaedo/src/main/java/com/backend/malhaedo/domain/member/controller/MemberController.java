package com.backend.malhaedo.domain.member.controller;

import com.backend.malhaedo.domain.member.converter.MemberConverter;
import com.backend.malhaedo.domain.member.dto.MemberRequestDTO;
import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.member.service.MemberService;
import com.backend.malhaedo.global.annotation.CurrentMember;
import com.backend.malhaedo.global.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/info")
    @Operation(summary = "카카오 로그인 설명 API", description = "카카아ㅗ 로그인 과정 설명입니다. <br />"
            + "1. (백엔드배포주소)/oauth2/authorization/kakao로 연결 <br />"
            + "2. 소셜 로그인 성공 시 프론트엔드에서 설정한 주소로 리다리엑트 됩니다. (주소는 따로 알려주세요) <br />")
    public ApiResponse<Void> signUp() {
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/signup/guest")
    @Operation(summary = "게스트 회원가입 API", description = "게스트 회원가입 API 입니다.")
    public ApiResponse<MemberResponseDTO.LoginSuccessDTO> guestSignUp(HttpServletResponse response) {
        Member member = MemberConverter.toJoinGuest();
        return ApiResponse.onSuccess(memberService.joinGuest(member, response));
    }

    @PostMapping("/profile")
    @Operation(summary = "프로필 설정 및 수정 API", description = "프로필 설정 및 수정 API 입니다. <br />"
            + "닉네임과 섬 이름을 설정 및 수정합니다.")
    public ApiResponse<Void> setProfile(@CurrentMember Member member,
                                        @Valid @RequestBody MemberRequestDTO.UpdateProfileRequestDTO request) {
        memberService.setProfile(request, member);
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃 API 입니다.")
    public ApiResponse<Void> logout(@CurrentMember Member member, HttpServletResponse response) {
        memberService.logout(member, response);
        return ApiResponse.onSuccess(null);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API 입니다.")
    public ApiResponse<Void> deleteMember(@CurrentMember Member member) {
        memberService.deleteMember(member);
        return ApiResponse.onSuccess(null);
    }
}
