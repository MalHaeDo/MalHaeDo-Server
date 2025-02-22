package com.backend.malhaedo.global.util;

import com.backend.malhaedo.global.error.code.status.ErrorStatus;
import com.backend.malhaedo.global.error.exception.GeneralException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final String COOKIE_NAME = "refresh_token";

    @Value("${Jwt.refresh-token.expiration-time}")
    private long refreshTokenExpirationTime;

    // 리프레쉬 토큰이 담긴 쿠키를 생성하는 메서드
    public Cookie createCookie(String refreshToken) {
        Cookie cookie = new Cookie(COOKIE_NAME, refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true); // 보안을 위해 httpOnly 설정
        cookie.setSecure(false); // HTTPS 적용 후 true로 변경 필요
        cookie.setMaxAge((int)refreshTokenExpirationTime);
        return cookie;
    }

    // 쿠키를 찾아 반환하는 메서드
    public Cookie getCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new GeneralException(ErrorStatus.EMPTY_TOKEN);
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(COOKIE_NAME)) {
                return cookie;
            }
        }

        throw new GeneralException(ErrorStatus.EMPTY_TOKEN);
    }

    // 쿠키를 삭제하는 메서드
    public Cookie deleteCookie() {
        Cookie cookie = new Cookie(COOKIE_NAME, "");
        cookie.setMaxAge(0); // 만료시간을 0으로 설정하여 삭제
        cookie.setPath("/"); // 삭제되는 쿠키의 경로 설정
        return cookie;
    }
}
