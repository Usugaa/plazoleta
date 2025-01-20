package com.microservicio.trazabilidad.infraestructure.security;

import com.microservicio.trazabilidad.domain.constants.RoleConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            String username = jwtService.extractUsername(token);
            Long role = jwtService.extractRole(token);

            List<SimpleGrantedAuthority> authorities = getAuthoritiesForRole(role);

            // Modificación aquí: incluimos el token como credenciales
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    token, // Agregamos el token aquí como credenciales
                    authorities
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Error al procesar el token: " + e.getMessage());
        }
    }

    private List<SimpleGrantedAuthority> getAuthoritiesForRole(Long role) {

        if (role.equals(RoleConstants.ADMIN)) {
            // Agregamos el prefijo ROLE_
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (role.equals(RoleConstants.OWNER)) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_OWNER"));
        } else if (role.equals(RoleConstants.EMPLOYEE)) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        } else if (role.equals(RoleConstants.CLIENT)) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT"));
        }

        return Collections.emptyList();
    }
}
