package com.restaurant.plazoleta.domain.usecase;

import com.restaurant.plazoleta.domain.api.IUserServicePort;
import com.restaurant.plazoleta.domain.model.User;
import com.restaurant.plazoleta.domain.spi.IUserPersistencePort;
import org.springframework.security.crypto.password.PasswordEncoder;


public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;

    public UserUseCase(IUserPersistencePort userPersistencePort, PasswordEncoder passwordEncoder) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User saveUser(User user) {
        // Validar si el documento ya existe
        if (userPersistencePort.existsByDocumentNumber(user.getDocumentNumber())) {
            throw new IllegalArgumentException("El documento ya está registrado.");
        }

        // Encriptar la contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Guardar el usuario
        return userPersistencePort.saveUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return userPersistencePort.getUserById(id);
    }

    @Override
    public User authenticate(String email, String password) {
        User user = userPersistencePort.getByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }

        // Verificar que la contraseña coincida
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta.");
        }

        return user;
    }
}