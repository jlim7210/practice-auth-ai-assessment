package com.example.practiceauth2.aop;

import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtSignatureExceptionHandler {

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleInvalidJwtSignatureException(HttpServletRequest request, SignatureException ex) {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName()) || "accessToken".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                }
            }
        }

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
