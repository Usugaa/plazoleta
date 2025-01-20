package com.microservicio.trazabilidad.infraestructure.output.jpa.configuration;

import com.microservicio.trazabilidad.infraestructure.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Rutas de Swagger/OpenAPI
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()

                        // Endpoints de trazabilidad - solo accesibles por CLIENT
                        .requestMatchers(HttpMethod.GET, "/traceability/client/{clientId}").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/traceability/order/{orderId}").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "/traceability/client/{clientId}/order/{orderId}").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "traceability/efficiency/orders/{restaurantId}").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "traceability/efficiency/employee/{restaurantId}").hasRole("OWNER")

                        // La ruta de guardado será accedida por el servicio de pedidos internamente
                        .requestMatchers(HttpMethod.POST, "/traceability/saveTraceability").permitAll()

                        // Configuración base
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
