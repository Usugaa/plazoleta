package com.restaurant.plazoleta.infraestructure.security;

import com.restaurant.plazoleta.infraestructure.output.jpa.entity.UserEntity;
import com.restaurant.plazoleta.infraestructure.output.jpa.repository.IUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    public CustomUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Aquí estamos creando una autoridad basada en el rol (que ahora es un Long)
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userEntity.getIdRole());

        // Pasamos el idRole (que es un Long) al constructor de CustomUserDetails
        return new CustomUserDetails(
                userEntity.getEmail(),
                userEntity.getPassword(),
                Collections.singletonList(authority), // Lista de autoridades con el rol del usuario
                userEntity.getIdRole() != null ? userEntity.getIdRole().getId() : null, // ID del rol (manejo de nulo)
                userEntity.getLastName(), // Apellido del usuario
                userEntity.getName() // Nombre del usuario
        );
    }
}
