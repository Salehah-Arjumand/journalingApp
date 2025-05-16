package com.example.journal.service;

import com.example.journal.entity.User;
import com.example.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            System.out.println("Password in DB: " + user.getPassword());

            return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().stream()
                            .map(Enum::name)
                            .toArray(String[]::new))
                    .build();

        } else {
            throw new UsernameNotFoundException("Username not found: " + username);
        }
    }
}

