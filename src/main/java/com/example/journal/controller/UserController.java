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

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findUserByUsername(username);
        if (userInDb != null) {
            userService.deleteUserById(userInDb.getId());
            log.info(LogMessages.DELETE_USER_BY_USERNAME, username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            log.warn(LogMessages.USER_WITH_USERNAME_NOT_FOUND, username);
            return new ResponseEntity<>("User with username: " + username + " not found!", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findUserByUsername(username);
        if (userInDb != null) {
            userInDb.setUsername(user.getUsername());
            userInDb.setPassword(user.getPassword());
            userService.updateUser(userInDb);
            log.info(LogMessages.UPDATE_USER_BY_USERNAME, username);
            return new ResponseEntity<>(userInDb, HttpStatus.ACCEPTED);
        } else {
            log.warn(LogMessages.USER_WITH_USERNAME_NOT_FOUND, username);
            return new ResponseEntity<>("User with user name: " + username + " not found!", HttpStatus.NOT_FOUND);
        }
    }


}