package com.restaurant.plazoleta.domain.usecase;


import com.restaurant.plazoleta.domain.constants.RoleConstants;
import com.restaurant.plazoleta.domain.model.User;
import com.restaurant.plazoleta.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_ShouldSaveUserSuccessfully() {
        // Arrange
        User user = new User();
        user.setDocumentNumber("123456");
        user.setPassword("plaintextpassword");
        user.setIdRole(RoleConstants.PROPIETARIO);

        when(userPersistencePort.existsByDocumentNumber("123456")).thenReturn(false);
        when(passwordEncoder.encode("plaintextpassword")).thenReturn("encryptedpassword");

        // Act
        User savedUser = userUseCase.saveUser(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals("encryptedpassword", savedUser.getPassword());
        assertEquals(RoleConstants.PROPIETARIO, savedUser.getIdRole());

        verify(userPersistencePort, times(1)).saveUser(user);
    }

    @Test
    void saveUser_ShouldThrowException_WhenDocumentNumberExists() {
        // Arrange
        User user = new User();
        user.setDocumentNumber("123456");

        when(userPersistencePort.existsByDocumentNumber("123456")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userUseCase.saveUser(user));
        assertEquals("El documento ya está registrado.", exception.getMessage());

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void saveUser_ShouldThrowException_WhenRoleIsNull() {
        // Arrange
        User user = new User();
        user.setDocumentNumber("123456");

        when(userPersistencePort.existsByDocumentNumber("123456")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userUseCase.saveUser(user));
        assertEquals("El rol no puede ser nulo.", exception.getMessage());

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void saveUser_ShouldThrowException_WhenRoleIsInvalid() {
        // Arrange
        User user = new User();
        user.setDocumentNumber("123456");
        user.setIdRole(999L); // Invalid role

        when(userPersistencePort.existsByDocumentNumber("123456")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userUseCase.saveUser(user));
        assertEquals("El rol ingresado no es válido: 999", exception.getMessage());

        verify(userPersistencePort, never()).saveUser(any());
    }
}