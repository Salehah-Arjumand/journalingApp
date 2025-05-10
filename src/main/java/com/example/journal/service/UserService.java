package com.example.journal.service;

import com.example.journal.entity.User;
import com.example.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> findUserById(String id){
        return Optional.ofNullable(userRepository.findById(id).orElse(null));
    }

    public void deleteUserById(String id){
        userRepository.deleteById(id);
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

}