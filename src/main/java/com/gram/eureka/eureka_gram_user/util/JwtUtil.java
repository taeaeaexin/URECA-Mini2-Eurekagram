package com.gram.eureka.eureka_gram_user.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 실무에서는 외부 주입 권장
    private final long validityInMilliseconds = 3600000; // 1시간

    //  토큰 생성
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(key)
                .compact();
    }

    // 전체 Claims 추출 (서명 & 만료 검증 포함)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 공통 클레임 추출
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    // 이메일(subject) 추출
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // role 추출 (통일된 스타일)
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // 만료 시간 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 유효한 토큰인지 확인 (서명 & 만료 포함)
    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token); // 내부에서 서명 + 만료 체크
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}