package com.restaurant.plazoleta.infraestructure.security;

import com.restaurant.plazoleta.infraestructure.exception.JwtAuthenticationException;
import com.restaurant.plazoleta.infraestructure.output.jpa.entity.UserEntity;
import com.restaurant.plazoleta.infraestructure.output.jpa.repository.IUserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final IUserRepository userRepository;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    @Value("${security.jwt.refresh-token.expiration}")
    private long refreshExpirationTime;

    public String generateTokenWithUserInfo(CustomUserDetails customUserDetails) {
        return generateToken(customUserDetails, expirationTime);
    }

    public String generateRefreshToken(CustomUserDetails customUserDetails) {
        return generateToken(customUserDetails, refreshExpirationTime);
    }

    private String generateToken(CustomUserDetails customUserDetails, long expiration) {
        // Obtener el ID del usuario desde la entidad o el contexto
        UserEntity userEntity = userRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrad  o"));

        return Jwts.builder()
                .setSubject(customUserDetails.getUsername())
                .claim("userId", userEntity.getId()) // Añadir ID del usuario
                .claim("role", customUserDetails.getRole())
                .claim("name", customUserDetails.getName())
                .claim("documentNumber", userEntity.getDocumentNumber()) // Usar el número de documento correcto
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    private java.security.Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractClaims(String token) {
        try {
            System.out.println("Secret Key: " + secretKey);
            System.out.println("Decoded Key Bytes Length: " + Base64.getDecoder().decode(secretKey).length);

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Full error details:");
            e.printStackTrace();
            throw new JwtAuthenticationException("Error al procesar el token: " + e.getMessage());
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

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Long extractRole(String token) {
        return extractClaims(token).get("role", Long.class);
    }

    public Long extractUserId(String token) {
        return extractClaims(token).get("userId", Long.class);
    }
}