package com.example.journal.service;

import com.example.journal.dto.UserRequestDTO;
import com.example.journal.entity.RolesEnum;
import com.example.journal.entity.User;
import com.example.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveNewUser(UserRequestDTO user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        String password = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(password);
        newUser.setRoles(List.of(RolesEnum.USER));
        userRepository.save(newUser);

    }

    public void updateUser(User user) {
        user.setUsername(user.getUsername());
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        user.setRoles(List.of(RolesEnum.USER));
        userRepository.save(user);

    }

    public void saveAdmin(UserRequestDTO user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        String password = passwordEncoder.encode(user.getPassword());
        newUser.setPassword(password);
        newUser.setRoles(List.of(RolesEnum.USER, RolesEnum.ADMIN));
        userRepository.save(newUser);

    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findUserById(String id){
        return userRepository.findById(id);
    }

    public void deleteUserById(String id){
        userRepository.deleteById(id);
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

}