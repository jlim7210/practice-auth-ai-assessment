package com.example.practiceauth2.service.impl;

import com.example.practiceauth2.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenServiceImplTest {

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private TokenServiceImpl tokenServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIssueCookieWithTokenByRefreshToken() {
        String refreshToken = "mockRefreshToken";
        String username = "testuser";
        String newAccessToken = "newAccessToken";

        when(jwtUtil.getUsernameFromToken(refreshToken)).thenReturn(username);
        when(jwtUtil.generateAccessToken(username)).thenReturn(newAccessToken);

        Cookie result = tokenServiceImpl.issueCookieWithTokenByRefreshToken(refreshToken);

        assertNotNull(result);
        assertEquals("accessToken", result.getName());
        assertEquals(newAccessToken, result.getValue());
        assertTrue(result.isHttpOnly());
        assertEquals("/", result.getPath());
        assertEquals(15 * 60, result.getMaxAge());

        verify(jwtUtil, times(1)).getUsernameFromToken(refreshToken);
        verify(jwtUtil, times(1)).generateAccessToken(username);
    }

    @Test
    public void testIssueCookieWithTokenByLogout() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        HttpServletResponse result = tokenServiceImpl.issueCookieWithTokenByLogout(response);

        assertNotNull(result);
        verify(response, times(2)).addCookie(any(Cookie.class));

        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        verify(response).addCookie(argThat(cookie ->
                "accessToken".equals(cookie.getName()) &&
                        cookie.getValue() == null &&
                        cookie.isHttpOnly() &&
                        "/".equals(cookie.getPath()) &&
                        cookie.getMaxAge() == 0
        ));

        verify(response).addCookie(argThat(cookie ->
                "refreshToken".equals(cookie.getName()) &&
                        cookie.getValue() == null &&
                        cookie.isHttpOnly() &&
                        "/".equals(cookie.getPath()) &&
                        cookie.getMaxAge() == 0
        ));
    }
}