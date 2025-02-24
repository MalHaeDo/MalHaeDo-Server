package com.backend.malhaedo.domain.member.controller;

import com.backend.malhaedo.domain.member.dto.MemberRequestDTO;
import com.backend.malhaedo.domain.member.dto.MemberResponseDTO;
import com.backend.malhaedo.domain.member.service.MemberService;
import com.backend.malhaedo.global.error.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/member")
public class MemberController {

    private MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "카카오 회원가입 API", description = "카카오 회원가입 API 입니다.")
    public ApiResponse<MemberResponseDTO.LoginSuccessDTO> login() {
        MemberResponseDTO.LoginSuccessDTO response = memberService.joinMember();
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/signup/guest")
    @Operation(summary = "게스트 회원가입 API", description = "게스트 회원가입 API 입니다.")
    public ApiResponse<MemberResponseDTO.LoginSuccessDTO> guestLogin() {
        MemberResponseDTO.LoginSuccessDTO response = memberService.joinMember();
        return ApiResponse.onSuccess(response);
    }

    @PostMapping("/{memberId}/profile")
    @Operation(summary = "프로필 설정 API", description = "프로필 설정 API 입니다. <br />"
            + "닉네임과 섬 이름을 설정합니다.")
    public ApiResponse<Void> setProfile(@PathVariable Long memberId,
                                        @Valid @RequestBody MemberRequestDTO.UpdateProfileRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @PatchMapping("/{memberId}/update/profile")
    @Operation(summary = "프로필 수정 API", description = "프로필 수정 API 입니다. <br />"
            + "닉네임과 섬 이름을 수정합니다.")
    public ApiResponse<Void> updateProfile(@PathVariable Long memberId,
                                           @Valid @RequestBody MemberRequestDTO.UpdateProfileRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/{memberId}/logout")
    @Operation(summary = "로그아웃 API", description = "로그아웃 API 입니다.")
    public ApiResponse<Void> logout(@PathVariable Long memberId) {
        return ApiResponse.onSuccess(null);
    }

    @DeleteMapping("/{memberId}/delete")
    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API 입니다.")
    public ApiResponse<Void> deleteMember(@PathVariable Long memberId) {
        return ApiResponse.onSuccess(null);
    }
}
