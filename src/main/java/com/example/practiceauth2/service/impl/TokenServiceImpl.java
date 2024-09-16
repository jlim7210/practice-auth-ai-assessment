package com.example.practiceauth2.service.impl;

import com.example.practiceauth2.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements com.example.practiceauth2.service.TokenService {

    private final JwtUtil jwtUtil;

    @Override
    public Cookie issueCookieWithTokenByRefreshToken(String refreshToken) {
        String newAccessToken = jwtUtil.generateAccessToken(jwtUtil.getUsernameFromToken(refreshToken));
        Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(15 * 60); // 15 minutes
        return accessTokenCookie;
    }

    @Override
    public HttpServletResponse issueCookieWithTokenByLogout(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setSecure(true); // Ensure this is set only if using HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(true); // Ensure this is set only if using HTTPS
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        return response;
    }
}
