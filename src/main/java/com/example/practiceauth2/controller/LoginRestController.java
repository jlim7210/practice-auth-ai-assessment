package com.example.practiceauth2.controller;

import com.example.practiceauth2.service.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class LoginRestController {

    private final TokenService tokenService;

    @PostMapping("/refresh-token")
    public void refreshToken(@RequestParam("refreshToken") String refreshToken, HttpServletResponse response) {
        Cookie accessTokenCookie = tokenService.issueCookieWithTokenByRefreshToken(refreshToken); // todo 실제 작동하는지 확인이 필요함
        response.addCookie(accessTokenCookie);
    }

    @GetMapping("/logout")
    public void logout(HttpServletResponse response) {
        tokenService.issueCookieWithTokenByLogout(response);

//        try {
//            response.sendRedirect("/login");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @GetMapping("/auth/status")
    public String getStatus(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Not authenticated";
        }
        return "Authenticated as " + authentication.getName();
    }
}
