package com.example.practiceauth2.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.lang.NonNull;
import com.example.practiceauth2.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException, SignatureException {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    final String token = cookie.getValue();
                    if (jwtUtil.isTokenExpired(token)) {
                        chain.doFilter(request, response);
                        return;
                    }

                    final Claims claims = jwtUtil.getClaimsFromToken(token);
                    final String username = claims.getSubject();

                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        if (jwtUtil.validateToken(token, userDetails)) {
                            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    String refreshToken = cookie.getValue();
                    try {
                        final String username = jwtUtil.extractUsername(refreshToken);
                        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        if (jwtUtil.validateToken(refreshToken, userDetails)) {
                            final String newAccessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
                            final Cookie newAccessTokenCookie = new Cookie("accessToken", newAccessToken);
                            newAccessTokenCookie.setHttpOnly(true);
                            newAccessTokenCookie.setPath("/");
                            response.addCookie(newAccessTokenCookie);

                            final Claims claims = jwtUtil.getClaimsFromToken(newAccessToken);
                            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    claims.getSubject(), null, null);
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                            chain.doFilter(request, response);
                            return;
                        }
                    } catch (ExpiredJwtException e) {
                        response.sendRedirect("/");
                        return;
                    }
                }
            }
        }
        response.sendRedirect("/");
    }
}