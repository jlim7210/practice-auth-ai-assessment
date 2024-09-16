package com.example.practiceauth2.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtil = Mockito.mock(JwtUtil.class);
    }

    @Test
    public void testGenerateAccessToken() {
        String username = "testuser";
        String mockToken = "mockAccessToken";

        when(jwtUtil.generateAccessToken(username)).thenReturn(mockToken);

        String token = jwtUtil.generateAccessToken(username);

        assertNotNull(token);
        assertEquals(mockToken, token);
    }

    @Test
    public void testGenerateRefreshToken() {
        String username = "testuser";
        String mockToken = "mockRefreshToken";

        when(jwtUtil.generateRefreshToken(username)).thenReturn(mockToken);

        String token = jwtUtil.generateRefreshToken(username);

        assertNotNull(token);
        assertEquals(mockToken, token);
    }

    @Test
    public void testGetClaimsFromToken() {
        String token = "mockToken";
        Claims mockClaims = Jwts.claims().setSubject("testuser");

        when(jwtUtil.getClaimsFromToken(token)).thenReturn(mockClaims);

        Claims claims = jwtUtil.getClaimsFromToken(token);

        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    public void testIsTokenExpired() {
        String token = "mockToken";

        when(jwtUtil.isTokenExpired(token)).thenReturn(true);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertTrue(isExpired);
    }

    @Test
    public void testGetUsernameFromToken() {
        String token = "mockToken";
        String mockUsername = "testuser";

        when(jwtUtil.getUsernameFromToken(token)).thenReturn(mockUsername);

        String username = jwtUtil.getUsernameFromToken(token);

        assertEquals(mockUsername, username);
    }
}