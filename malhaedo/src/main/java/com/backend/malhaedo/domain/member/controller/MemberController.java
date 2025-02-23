package com.backend.malhaedo.domain.member.controller;

import com.backend.malhaedo.global.error.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/member")
public class MemberController {

    @PostMapping("/signup")
    public ApiResponse<Void> loginPage() {
        return ApiResponse.onSuccess(null);
    }
}
