package com.example.journal.controller;

import com.example.journal.entity.User;
import com.example.journal.utils.LogMessages;
import com.example.journal.service.JournalEntryService;
import com.example.journal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
                log.info(LogMessages.UPDATE_USER_BY_ID, id);
                return new ResponseEntity<>(userInDb, HttpStatus.OK);
            } else {
                log.warn(LogMessages.USER_WITH_ID_NOT_FOUND, id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            log.warn(LogMessages.ACCESS_DENIED);
            return new ResponseEntity<>("Access denied. You are not authorized to get user with id: " + id, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

            User userInDb = userService.findUserByUsername(username);
            if (userInDb != null) {
                log.info(LogMessages.GET_USER_BY_USERNAME, username);
                return new ResponseEntity<>(userInDb, HttpStatus.OK);
            } else {
                log.warn(LogMessages.USER_WITH_USERNAME_NOT_FOUND, username);
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
                log.info(LogMessages.DELETE_USER_BY_ID, id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.warn(LogMessages.USER_WITH_ID_NOT_FOUND, id);
                return new ResponseEntity<>("User with id: " + id + " not found!", HttpStatus.NOT_FOUND);
            }
        } else {
            log.warn(LogMessages.ACCESS_DENIED);
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
            log.info(LogMessages.UPDATE_USER_BY_USERNAME, username);
            return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
        } else {
            log.warn(LogMessages.USER_WITH_USERNAME_NOT_FOUND, username);
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
                log.info(LogMessages.UPDATE_USER_BY_ID, id);
                return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
            } else {
                log.warn(LogMessages.USER_WITH_ID_NOT_FOUND, id);
                return new ResponseEntity<>("User with id: " + id + " not found!", HttpStatus.NOT_FOUND);
            }
        } else {
            log.warn(LogMessages.ACCESS_DENIED);
            return new ResponseEntity<>("Access denied. You are not authorized to update user with id: " + id, HttpStatus.FORBIDDEN);
        }

    }


}