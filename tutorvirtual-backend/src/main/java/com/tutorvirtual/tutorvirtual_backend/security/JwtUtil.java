package com.tutorvirtual.tutorvirtual_backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // 🔑 clave segura (mínimo 256 bits)
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "tutorvirtual_secret_key_1234567890".getBytes()
    );

    // ⏱️ 1 hora
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String generateToken(String nombreUsuario) {
        return Jwts.builder()
                .setSubject(nombreUsuario)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
    public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
}

private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody();
}

}
