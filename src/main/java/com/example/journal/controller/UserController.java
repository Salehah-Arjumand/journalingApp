package com.example.journal.controller;

import com.example.journal.entity.User;
import com.example.journal.service.JournalEntryService;
import com.example.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (userService.findUserByUsername(username).getId().equals(id)) {
            User userInDb = userService.findUserById(id).orElse(null);
            if (userInDb != null) {
                return new ResponseEntity<>(userInDb, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Access denied. You are not authorized to get user with id: " + id, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

            User userInDb = userService.findUserByUsername(username);
            if (userInDb != null) {
                return new ResponseEntity<>(userInDb, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (userService.findUserByUsername(username).getId().equals(id)) {
            User userInDb = userService.findUserById(id).orElse(null);
            if (userInDb != null) {
                userService.deleteUserById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>("User with id: " + id + " not found!", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Access denied. You are not authorized to delete user with id: " + id, HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/username")
    public ResponseEntity<?> updateUserByUsername(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findUserByUsername(username);
        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.saveNewUser(userInDb);
            return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("User with user name: " + username + " not found!", HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable String id, @RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (userService.findUserByUsername(username).getId().equals(id)) {
            User userInDb = userService.findUserById(id).orElse(null);
            if (userInDb != null) {
                userInDb.setUsername(user.getUsername());
                userInDb.setPassword(user.getPassword());
            userService.saveNewUser(userInDb);
                return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("User with id: " + id + " not found!", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Access denied. You are not authorized to update user with id: " + id, HttpStatus.FORBIDDEN);
        }

    }

}