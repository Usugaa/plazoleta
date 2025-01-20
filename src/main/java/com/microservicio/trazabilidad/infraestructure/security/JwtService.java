package com.microservicio.trazabilidad.infraestructure.security;

import com.microservicio.trazabilidad.domain.constants.RoleConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extracting claims: ", e);
            throw new IllegalArgumentException("Invalid token: " + e.getMessage(), e);
        }
    }

    private java.security.Key getSigningKey() {
        try {
            return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        } catch (Exception e) {
            log.error("Error generating signing key", e);
            throw new IllegalArgumentException("Error generating signing key", e);
        }
    }

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public Long extractRole(String token) {
        try {
            Claims claims = extractClaims(token);
            Object roleObj = claims.get("role");

            if (roleObj instanceof Number number) {
                Long role = number.longValue();
                if (role.equals(RoleConstants.CLIENT)) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Invalid role");
        } catch (Exception e) {
            log.error("Error extracting role: ", e);
            throw new IllegalArgumentException("Error processing role from token", e);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}