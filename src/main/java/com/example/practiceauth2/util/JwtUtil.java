package com.example.practiceauth2.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY; // todo : 주기적으로 변경

    @Value("${jwt.token.access.expiration}")
    private long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${jwt.token.refresh.expiration}")
    private long REFRESH_TOKEN_EXPIRATION_TIME;

//    @Value("${jwt.salt}")
//    private String SALT; // todo : 주기적으로 변경

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(this.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new SignatureException("Invalid JWT signature");
        }
    }

    public boolean isTokenExpired(String token) {
        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String extractUsername(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//    todo : salted key.
//    private String getSaltedKey() {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256"); // todo encrypting algorithm
//            byte[] hash = digest.digest((SECRET_KEY + SALT).getBytes(StandardCharsets.UTF_8));
//            return Base64.getEncoder().encodeToString(hash);
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error generating salted secret key", e);
//        }
//    }
}