package com.example.journal.service;

import com.example.journal.entity.RolesEnum;
import com.example.journal.entity.User;
import com.example.journal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


@SpringBootTest
public class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void loadUserByUsernameTest() {
        User mockUser = new User();
        mockUser.setUsername("Haris");
        mockUser.setPassword("haris");
        mockUser.setRoles(List.of(RolesEnum.USER));

        when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(mockUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("Haris");
        assertNotNull(userDetails);
    }

}

