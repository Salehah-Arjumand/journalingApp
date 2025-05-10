package com.example.journal.controller;

import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.service.JournalEntryService;
import com.example.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping("users/{username}/journals")
    public ResponseEntity<?> createJournalForUser(@PathVariable String username, @RequestBody JournalEntry journalEntry) {
        try {
            if (userService.findUserByUsername(username) != null) {
                if (journalEntryService.findJournalEntryById(journalEntry.getId()) != null) {
                    return new ResponseEntity<>("Journal entry with id: " + journalEntry.getId() + " already exists!", HttpStatus.BAD_REQUEST);
                } else {
                    journalEntryService.saveJournalEntry(username, journalEntry);
                    return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
                }
            } else {
                return new ResponseEntity<>("User with username: " + username + " does not exist!", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("users/{username}/journals")
    public ResponseEntity<?> getAllJournalEntriesByUser(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (user != null) {
            List<JournalEntry> all = user.getJournalEntries();
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/journals/{journalId}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable("journalId") String journalId) {
        JournalEntry j = journalEntryService.findJournalEntryById(journalId);
        if (j != null) {
            return new ResponseEntity<>(j, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Journal entry with id: " + journalId + " does not exist!", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/users/{username}/journals/{journalId}")
    public ResponseEntity<?> deleteJournalEntryFromUser(@PathVariable String username, @PathVariable String journalId) {
        journalEntryService.deleteJournalEntryFromUser(journalId, username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/users/{username}/journals/{journalId}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable String username, @PathVariable String journalId, @RequestBody JournalEntry newEntry) {
        if (userService.findUserByUsername(username) != null) {
            JournalEntry oldEntry = journalEntryService.findJournalEntryById(journalId);
            if (oldEntry != null) {
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
                journalEntryService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Journal entry with id: " + journalId + " does not exist!", HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>("User with username: " + username + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }
}
