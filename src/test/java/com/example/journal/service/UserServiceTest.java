package com.example.journal.service;

import com.example.journal.dto.UserRequestDTO;
import com.example.journal.entity.RolesEnum;
import com.example.journal.entity.User;
import com.example.journal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveNewUser() {
        UserRequestDTO userDto = new UserRequestDTO();
        userDto.setUsername("testuser");
        userDto.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.saveNewUser(userDto);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("testuser", savedUser.getUsername());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(RolesEnum.USER));
    }

    @Test
    public void testSaveAdmin() {
        UserRequestDTO userDto = new UserRequestDTO();
        userDto.setUsername("admin");
        userDto.setPassword("adminpass");

        when(passwordEncoder.encode("adminpass")).thenReturn("encodedAdminPass");
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.saveAdmin(userDto);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("admin", savedUser.getUsername());
        assertEquals("encodedAdminPass", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(RolesEnum.ADMIN));
        assertTrue(savedUser.getRoles().contains(RolesEnum.USER));
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        User user = new User();
        user.setUsername("updateduser");
        user.setPassword("newPassword");

        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        userService.updateUser(user);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("updateduser", savedUser.getUsername());
        assertEquals("encodedNewPassword", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(RolesEnum.USER));
        assertEquals(1, savedUser.getRoles().size());
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    public void testFindUserById() {
        User user = new User();
        user.setId("123");
        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findUserById("123");

        assertTrue(foundUser.isPresent());
        assertEquals("123", foundUser.get().getId());
    }

    @Test
    public void testDeleteUserById() {
        userService.deleteUserById("456");
        verify(userRepository, times(1)).deleteById("456");
    }

    @Test
    public void testFindUserByUsername() {
        User user = new User();
        user.setUsername("john");
        when(userRepository.findByUsername("john")).thenReturn(user);

        User foundUser = userService.findUserByUsername("john");

        assertNotNull(foundUser);
        assertEquals("john", foundUser.getUsername());
    }
}
