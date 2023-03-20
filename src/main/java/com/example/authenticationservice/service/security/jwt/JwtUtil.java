package com.example.authenticationservice.service.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
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
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
        } catch (UnsupportedJwtException exception) {
            log.error("the claimsJws argument does not represent an Claims JWS", exception);
        } catch (MalformedJwtException exception) {
            log.error("Invalid token provided", exception);
        } catch (SignatureException exception) {
            log.error("Invalid signature", exception);
        } catch (ExpiredJwtException exception) {
            log.error("Token has expired", exception);
        } catch (IllegalArgumentException exception) {
            log.error("the claimsJws string is null or empty or only whitespace", exception);
        }
    }
}