package com.example.journal.controller;

import com.example.journal.dto.UserRequestDTO;
import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.utils.LogMessages;
import com.example.journal.service.JournalEntryService;
import com.example.journal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    JournalEntryService journalEntryService;

    @GetMapping("/journals")
    public ResponseEntity<?> getAllJournalEntries() {
        List<JournalEntry> allEntries = journalEntryService.getAllJournalEntries();
        return new ResponseEntity<>(allEntries, HttpStatus.OK);

    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody UserRequestDTO user) {
        try {
            if (userService.findUserByUsername(user.getUsername()) != null) {
                log.warn(LogMessages.USER_WITH_USERNAME_EXISTS, user.getUsername());
                return new ResponseEntity<>("User with username: " + user.getUsername() + " already exists!", HttpStatus.BAD_REQUEST);
            } else {
                userService.saveAdmin(user);
                log.info(LogMessages.CREATE_ADMIN, user.getUsername());
                return new ResponseEntity<>(user, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
