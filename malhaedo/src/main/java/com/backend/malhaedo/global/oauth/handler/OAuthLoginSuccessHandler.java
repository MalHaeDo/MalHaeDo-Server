package com.backend.malhaedo.global.oauth.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.backend.malhaedo.domain.member.converter.MemberConverter;
import com.backend.malhaedo.domain.member.entity.Member;
import com.backend.malhaedo.domain.member.repository.MemberRepository;
import com.backend.malhaedo.global.jwt.principal.PrincipalDetails;
import com.backend.malhaedo.global.util.CookieUtil;
import com.backend.malhaedo.global.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${Jwt.redirect}")
    private String REDIRECT_URI;

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
//        PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
//        Member member = principalDetails.getMember();
        Member member = extractMember(authentication);

        log.info("oauthloginsuccesshandler 실행됨");

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

    private Member extractMember(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof PrincipalDetails principalDetails) {
            log.info("일반 로그인 사용자 감지: memberId={}", principalDetails.getMember().getMemberId());
            return principalDetails.getMember();
        } else if (principal instanceof DefaultOidcUser oidcUser) {
            log.info("Google OIDC 로그인 감지: {}", oidcUser.getAttributes());

            // Google OIDC의 경우 "sub" 값을 사용하여 회원 조회
            String providerId = oidcUser.getAttribute("sub");

            return memberRepository.findByProviderAndProviderId("google", providerId)
                    .orElseGet(() -> {
                        log.info("🆕 새로운 Google 사용자를 자동 회원가입 진행 - providerId: {}", providerId);
                        return memberRepository.save(MemberConverter.toSocialMember("google", providerId));
                    });
        } else {
            log.error("알 수 없는 인증 객체: {}", principal.getClass().getName());
            throw new IllegalStateException("지원되지 않는 인증 타입입니다.");
        }
    }


    private String setRedirectUri(String accessToken) {
        String encodedToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
        String redirectUri = REDIRECT_URI + "?access_token=" + encodedToken;

        log.info("최종 Redirect URL: {}", redirectUri); // 로그 추가
        return redirectUri;
    }
}
