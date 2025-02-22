package com.backend.malhaedo.global.oauth.handler;

import java.io.IOException;

import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.member.repository.MemberRepository;
import com.backend.malhaedo.global.jwt.principal.PrincipalDetails;
import com.backend.malhaedo.global.util.CookieUtil;
import com.backend.malhaedo.global.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${Jwt.redirect}")
    private String REDIRECT_URI;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
        Member member = principalDetails.getMember();

        // 리프레시 토큰 발급
        String refreshToken = jwtUtil.generateRefreshToken(member.getMemberId());

        // response에 cookie로 반환
        Cookie cookie = cookieUtil.createCookie(refreshToken);
        response.addCookie(cookie);

        // 액세스 토큰 발급
        String accessToken = jwtUtil.generateAccessToken(member.getMemberId());

        // 액세스 토큰, role, venueId를 담아 리다이렉트 uri 생성
        String redirectUri = setRedirectUri(accessToken);

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

    private String setRedirectUri(String accessToken) {
        String redirectUri = String.format(REDIRECT_URI, accessToken);
        return redirectUri;
    }
}
