package com.example.journal.controller;

import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.service.JournalEntryService;
import com.example.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController

@RequestMapping("/api/journals")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createJournalForUser(@RequestBody JournalEntry journalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {

                if (journalEntryService.findJournalEntryById(journalEntry.getId()) != null) {
                    return new ResponseEntity<>("Journal entry with id: " + journalEntry.getId() + " already exists!", HttpStatus.BAD_REQUEST);
                } else {
                    journalEntryService.saveJournalEntry(username, journalEntry);
                    return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
                }

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        if (user != null) {
            List<JournalEntry> all = user.getJournalEntries();
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<?> getUserJournalEntryById(@PathVariable("journalId") String journalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findUserByUsername(username);
        if (user != null) {
            JournalEntry j = user.getJournalEntryById(journalId);
            if (j != null) {
                return new ResponseEntity<>(j, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Journal entry with id: " + journalId + " does not exist for user with username: " + username + "!", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User with username: " + username + " does not exist!", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<?> deleteJournalEntryFromUser(@PathVariable String journalId) {
        int x = 2;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        journalEntryService.deleteJournalEntryFromUser(journalId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{journalId}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable String journalId, @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (userService.findUserByUsername(username) != null) {
            JournalEntry oldEntry = journalEntryService.findJournalEntryById(journalId);
            if (oldEntry != null) {
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setDate(LocalDateTime.now());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Journal entry with id: " + journalId + " does not exist for user with username: " + username + "!", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User with username: " + username + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }
}
