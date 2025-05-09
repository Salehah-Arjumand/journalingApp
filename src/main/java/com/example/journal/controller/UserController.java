package com.example.journal.controller;

import com.example.journal.entity.User;
import com.example.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (userService.findById(user.getId()).isPresent()) {
                return new ResponseEntity<>("User with id: " + user.getId() + " already exists!", HttpStatus.BAD_REQUEST);
            } else if (userService.findByUsername(user.getUsername()) != null) {
                return new ResponseEntity<>("User with username: " + user.getUsername() + " already exists!", HttpStatus.BAD_REQUEST);
            } else {
                userService.saveUser(user);
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        User j = userService.findById(id).orElse(null);
        if (j != null) {
            return new ResponseEntity<>(j, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id) {
        User userInDb = userService.findById(id).orElse(null);
        if (userInDb != null) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("User with id: " + id + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("username/{username}")
    public ResponseEntity<?> updateUserByUsername(@PathVariable String username, @RequestBody User user) {
        User userInDb = userService.findByUsername(username);
        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.saveUser(userInDb);
            return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("User with user name: " + username + " not found!", HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable String id, @RequestBody User user) {
        User userInDb = userService.findById(id).orElse(null);
        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.saveUser(userInDb);
            return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("User with id: " + id + " not found!", HttpStatus.NOT_FOUND);
        }

    }
}