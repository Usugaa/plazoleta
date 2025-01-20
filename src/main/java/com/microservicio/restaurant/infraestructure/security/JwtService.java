package com.microservicio.restaurant.infraestructure.security;

import com.microservicio.restaurant.domain.constants.RoleConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import io.jsonwebtoken.security.Keys;

import java.util.Base64;

@Component
@Slf4j
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Token inválido: " + e.getMessage(), e);
        }
    }

    private java.security.Key getSigningKey() {
        try {
            return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
        } catch (Exception e) {
            log.error("Error generando clave de firma", e);  // Usar log en lugar de e.printStackTrace()
            throw new IllegalArgumentException("Error generando clave de firma", e);
        }
    }

    public String extractUsername(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    public Long extractUserId(String token) {
        try {
            // Añade logging para entender qué está pasando
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Log adicional para ver los claims
            log.info("Claims extraídos: {}", claims);

            // Asegúrate de que el claim de ID de usuario existe
            Object userIdClaim = claims.get("userId");
            log.info("User ID Claim: {}", userIdClaim);

            if (userIdClaim == null) {
                throw new IllegalArgumentException("No se encontró el ID de usuario en el token");
            }

            // Convierte el claim a Long
            return Long.parseLong(userIdClaim.toString());
        } catch (Exception e) {
            log.error("Error al extraer el ID de usuario: {}", e.getMessage());
            throw new IllegalArgumentException("Token inválido o expirado", e);
        }
    }

    public Long extractRole(String token) {
        try {
            Claims claims = extractClaims(token);
            Object roleObj = claims.get("role");

            if (roleObj instanceof Number number) {
                Long role = number.longValue();

                if (role.equals(RoleConstants.ADMIN) ||
                        role.equals(RoleConstants.OWNER) ||
                        role.equals(RoleConstants.EMPLOYEE) ||
                        role.equals(RoleConstants.CLIENT)) {
                    return role;
                }
            }

            throw new IllegalArgumentException("Invalid role");
        } catch (Exception e) {
            log.error("Error al extraer el rol: {}", e.getMessage());
            throw new IllegalArgumentException("Error procesando el rol del token", e);
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
