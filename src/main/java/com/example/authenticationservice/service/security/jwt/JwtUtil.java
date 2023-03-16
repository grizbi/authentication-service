package com.example.authenticationservice.service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.validity}")
    private Long tokenValidity;

    public Claims getClaims(final String jwtToken) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(jwtToken).getBody();
    }

    public String generateToken(String username) {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuer("CodeJava")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();
    }

    public void validateToken(final String token) {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
    }
}