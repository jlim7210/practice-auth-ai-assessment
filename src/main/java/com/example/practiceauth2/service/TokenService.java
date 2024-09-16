package com.example.practiceauth2.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    Cookie issueCookieWithTokenByRefreshToken(String refreshToken);

    HttpServletResponse issueCookieWithTokenByLogout(HttpServletResponse response);
}
