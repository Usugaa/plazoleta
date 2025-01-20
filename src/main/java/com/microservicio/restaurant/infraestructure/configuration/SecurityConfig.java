package com.microservicio.restaurant.infraestructure.configuration;

import com.microservicio.restaurant.infraestructure.security.JwtAuthenticationFilter;
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

                        // EndPoints de restaurante
                        .requestMatchers(HttpMethod.POST, "/restaurant/saveRestaurant").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "restaurant/getRestaurants").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.GET, "restaurant/efficiency/{restaurantId}").hasRole("EMPLOYEE")

                        // Endpoints de platos
                        .requestMatchers(HttpMethod.POST, "/platos").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "/platos/{id}").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/platos/{id}").hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT, "/platos/{id}/status").hasRole("OWNER")
                        .requestMatchers(HttpMethod.GET, "orders/byStatus").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "orders/assign/{orderId}").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "orders/{orderId}/ready").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "orders/{orderId}/deliver").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/platos/restaurante/{idRestaurant}").hasAnyRole("OWNER", "EMPLOYEE", "CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/orders/{orderId}/cancel").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.POST, "/orders/saveOrder").hasRole("CLIENT")

                        // Configuración base
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}