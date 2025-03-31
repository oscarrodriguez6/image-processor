package com.imageProcessor.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "dikjc_efdweiDWQAWD54w_2--!!s54Wsdkmvsd.lSDVsldcsddhcbASSDE%"; // 🔥 Cambiar por una segura
    private final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generarToken(String email) {
        return Jwts.builder()
                .claim("sub", email) // Usamos .claim en lugar de .setSubject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String obtenerEmailDesdeToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}

