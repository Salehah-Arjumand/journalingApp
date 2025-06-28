package com.example.journal.service;

import com.example.journal.entity.RolesEnum;
import com.example.journal.entity.User;
import com.example.journal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Disabled
@SpringBootTest
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
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.saveNewUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.getRoles().contains(RolesEnum.USER));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testSaveAdmin() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("adminpass");

        when(passwordEncoder.encode("adminpass")).thenReturn("encodedAdminPass");

        userService.saveAdmin(user);

        assertEquals("encodedAdminPass", user.getPassword());
        assertTrue(user.getRoles().contains(RolesEnum.ADMIN));
        assertTrue(user.getRoles().contains(RolesEnum.USER));
        verify(userRepository, times(1)).save(user);
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
