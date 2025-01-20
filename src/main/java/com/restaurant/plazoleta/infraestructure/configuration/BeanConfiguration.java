package com.restaurant.plazoleta.infraestructure.configuration;

import com.restaurant.plazoleta.application.mapper.RoleRequestMapper;
import com.restaurant.plazoleta.application.mapper.UserResponseMapper;
import com.restaurant.plazoleta.domain.api.IRoleServicePort;
import com.restaurant.plazoleta.domain.api.IUserServicePort;
import com.restaurant.plazoleta.domain.spi.IRolePersistencePort;
import com.restaurant.plazoleta.domain.spi.IUserPersistencePort;
import com.restaurant.plazoleta.domain.usecase.RoleUseCase;
import com.restaurant.plazoleta.domain.usecase.UserUseCase;
import com.restaurant.plazoleta.infraestructure.output.jpa.adapter.RoleJpaAdapter;
import com.restaurant.plazoleta.infraestructure.output.jpa.adapter.UserJpaAdapter;
import com.restaurant.plazoleta.infraestructure.output.jpa.mapper.RoleEntityMapper;
import com.restaurant.plazoleta.infraestructure.output.jpa.mapper.UserEntityMapper;
import com.restaurant.plazoleta.infraestructure.output.jpa.repository.IRoleRepository;
import com.restaurant.plazoleta.infraestructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final UserEntityMapper userEntityMapper;
    private final RoleEntityMapper roleEntityMapper;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, userEntityMapper);
    }

    @Bean
    public IRolePersistencePort rolePersistencePort() {
        return new RoleJpaAdapter(roleRepository, roleEntityMapper);
    }

    @Bean
    public RoleRequestMapper roleRequestMapper() {
        return Mappers.getMapper(RoleRequestMapper.class);
    }

    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort, PasswordEncoder passwordEncoder) {
        return new UserUseCase(userPersistencePort, passwordEncoder);
    }

    @Bean
    public IRoleServicePort roleServicePort() {
        return new RoleUseCase(rolePersistencePort());
    }

    @Bean
    public UserResponseMapper userResponseMapper(){
        return new UserResponseMapper();
    }
}