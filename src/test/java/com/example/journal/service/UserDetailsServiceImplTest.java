package com.example.journal.service;

import com.example.journal.entity.RolesEnum;
import com.example.journal.entity.User;
import com.example.journal.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void loadUserByUsernameTest() {
        User mockUser = new User();
        mockUser.setUsername("Haris");
        mockUser.setPassword("haris");
        mockUser.setRoles(List.of(RolesEnum.USER));

        when(userRepository.findByUsername("Haris")).thenReturn(mockUser);

        userDetailsService = new UserDetailsServiceImpl();
        userDetailsService.setUserRepository(userRepository);

        UserDetails userDetails = userDetailsService.loadUserByUsername("Haris");

        assertNotNull(userDetails);
    }
}


