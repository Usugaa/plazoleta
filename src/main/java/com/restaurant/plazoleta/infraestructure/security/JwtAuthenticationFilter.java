package com.restaurant.plazoleta.infraestructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        System.out.println("Debug - Auth Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            System.out.println("Debug - Token: " + token);
            System.out.println("Debug - Token length: " + token.length());

            boolean tokenValid = jwtService.isTokenValid(token);
            System.out.println("Debug - Token Valid: " + tokenValid);

            if (!tokenValid) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado o inválido");
                return;
            }

            String username = jwtService.extractUsername(token);
            Long role = jwtService.extractRole(token);
            Long userId = jwtService.extractUserId(token);

            System.out.println("Debug - Extracted Username: " + username);
            System.out.println("Debug - Extracted Role: " + role);
            System.out.println("Debug - Extracted UserId: " + userId);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Crear UserDetails personalizado
                UserDetails userDetails = new CustomUserDetails(
                        username,
                        "",
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)),
                        role,
                        "", // documentNumber
                        username // name
                );

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        token, // Pasar el token como credenciales
                        userDetails.getAuthorities()
                );

                // Añadir información adicional
                Map<String, Object> details = new HashMap<>();
                details.put("userId", userId);
                details.put("role", role);

                authToken.setDetails(details);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            System.out.println("Debug - Full Exception:");
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error al procesar el token: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}