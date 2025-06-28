package com.example.journal.controller;

import com.example.journal.dto.UserRequestDTO;
import com.example.journal.entity.User;
import com.example.journal.utils.LogMessages;
import com.example.journal.service.JournalEntryService;
import com.example.journal.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/public")
public class PublicUserController {

    @Autowired
    UserService userService;

    @Autowired
    JournalEntryService journalEntryService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequestDTO user) {
        try {
            if (userService.findUserByUsername(user.getUsername()) != null) {
                log.warn(LogMessages.USER_WITH_USERNAME_EXISTS, user.getUsername());
                return new ResponseEntity<>("User with username: " + user.getUsername() + " already exists!", HttpStatus.BAD_REQUEST);
            } else {
                userService.saveNewUser(user);
                log.info(LogMessages.CREATE_USER, user.getUsername());
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
