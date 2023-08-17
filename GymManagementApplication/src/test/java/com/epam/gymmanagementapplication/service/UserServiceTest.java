package com.epam.gymmanagementapplication.service;

import com.epam.gymmanagementapplication.dto.UserDTO;
import com.epam.gymmanagementapplication.dto.request.ModifyPassword;
import com.epam.gymmanagementapplication.dto.response.Credential;
import com.epam.gymmanagementapplication.exception.UserException;
import com.epam.gymmanagementapplication.model.User;
import com.epam.gymmanagementapplication.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testSaveUser() throws UserException {
        // Create mock data
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUsername("johndoe");
        userDTO.setPassword("password");
        userDTO.setEMail("sid@gmail.com");

        User mockUser = new User();
        mockUser.setUsername(userDTO.getUsername());

        // Mock behavior
        Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Test
        User result = userService.saveUser(userDTO);

        // Verification
        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());

        // Verify mock interactions
        Mockito.verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void testSaveUserExceptionCase() throws UserException {
        // Create mock data
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUsername("johndoe");
        userDTO.setPassword("password");

        User mockUser = new User();
        mockUser.setUsername(userDTO.getUsername());

        // Test
        assertThrows(UserException.class, () -> userService.saveUser(userDTO));
    }


    @Test
    void testLoginUserValid() throws UserException {
        // Create mock data
        String username = "johndoe";
        String password = "password";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(password);

        // Mock behavior
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Test
        boolean isValid = userService.loginUser(new Credential(username, password));

        // Verification
        assertTrue(isValid);

        // Verify mock interactions
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void testLoginUserInvalidUsername() throws UserException {
        // Create mock data
        String username = "johndoe";
        String password = "password";

        // Mock behavior
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Test
        assertThrows(UserException.class, () -> userService.loginUser(new Credential(username, password)));
    }


    @Test
    void testChangeLoginValid() throws UserException {
        // Create mock data
        String username = "johndoe";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(oldPassword);

        ModifyPassword modifyPassword = new ModifyPassword();
        modifyPassword.setUsername(username);
        modifyPassword.setOldPassword(oldPassword);
        modifyPassword.setNewPassword(newPassword);

        // Mock behavior
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        Mockito.when(passwordEncoder.matches(oldPassword, mockUser.getPassword())).thenReturn(true);

        // Test
        boolean result = userService.changeLogin(modifyPassword);

        // Verification
        assertTrue(result);

        // Verify mock interactions
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }


    @Test
    void testChangeLoginValidPasswordMismatch() throws UserException {
        // Create mock data
        String username = "johndoe";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(oldPassword);

        ModifyPassword modifyPassword = new ModifyPassword();
        modifyPassword.setUsername(username);
        modifyPassword.setOldPassword(oldPassword);
        modifyPassword.setNewPassword(newPassword);

        // Mock behavior
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        Mockito.when(passwordEncoder.matches(oldPassword, mockUser.getPassword())).thenReturn(false);

        // Test
        boolean result = userService.changeLogin(modifyPassword);

        // Verification
        assertFalse(result);

        // Verify mock interactions
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }


    @Test
    void testChangeLoginInvalidUsername() throws UserException {
        // Create mock data
        String username = "johndoe";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        ModifyPassword modifyPassword = new ModifyPassword();
        modifyPassword.setUsername(username);
        modifyPassword.setOldPassword(oldPassword);
        modifyPassword.setNewPassword(newPassword);

        // Mock behavior
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Test
        assertThrows(UserException.class, () -> userService.changeLogin(modifyPassword));
    }

    @Test
    void testChangeLoginPasswordsMatch() throws UserException {
        // Create mock data
        String username = "johndoe";
        String password = "password";

        ModifyPassword modifyPassword = new ModifyPassword();
        modifyPassword.setUsername(username);
        modifyPassword.setOldPassword(password);
        modifyPassword.setNewPassword(password);

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(password);


        // Test
        assertThrows(UserException.class, () -> userService.changeLogin(modifyPassword));
    }


    @Test
    void testGetUser() throws UserException {
        // Create mock data
        String username = "johndoe";

        User mockUser = new User();
        mockUser.setUsername(username);

        // Mock behavior
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Test
        User result = userService.getUser(username);

        // Verification
        assertEquals(mockUser, result);

        // Verify mock interactions
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(username);
    }

    @Test
    void testGetUserNotFound() throws UserException {
        // Create mock data
        String username = "johndoe";

        // Mock behavior
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Test
        assertThrows(UserException.class, () -> userService.getUser(username));
    }


    @Test
    void removeUser() {
        Mockito.doNothing().when(userRepository).delete(any(User.class));

        assertDoesNotThrow(() -> userService.removeUser(new User()));

        Mockito.verify(userRepository).delete(any(User.class));
    }
}
