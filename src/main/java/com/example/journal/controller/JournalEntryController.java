package com.example.journal.controller;

import com.example.journal.dto.JournalEntryRequestDTO;
import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.service.QuoteService;
import com.example.journal.utils.LogMessages;
import com.example.journal.service.JournalEntryService;
import com.example.journal.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController

@RequestMapping("/api/journals")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuoteService quoteService;


    @PostMapping
    public ResponseEntity<?> createJournalForUser(@RequestBody @Valid JournalEntryRequestDTO journalEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {

                    journalEntryService.saveJournalEntry(username, journalEntry);
                    log.info(LogMessages.CREATE_JOURNAL, journalEntry.getTitle(), username);
                    String randomQuote = quoteService.getRandomQuote();
            String message =
                    "Your journal entry " + journalEntry.getTitle() + " has been saved successfully!\nHere's something to inspire you today:\n" +
                    randomQuote;
            return new ResponseEntity<>(message, HttpStatus.CREATED);

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
            log.info(LogMessages.GET_ALL_JOURNALS_BY_USER, username);
            List<JournalEntry> all = user.getJournalEntries();
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            log.warn(LogMessages.USER_WITH_USERNAME_NOT_FOUND, username);
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
                log.info(LogMessages.GET_JOURNAL_BY_ID_AND_USER, journalId, username);
                return new ResponseEntity<>(j, HttpStatus.OK);
            } else {
                log.info(LogMessages.JOURNAL_NOT_FOUND_FOR_USER, journalId, username);

                return new ResponseEntity<>("Journal entry with ID: " + journalId + " does not exist for user with username: " + username + "!", HttpStatus.NOT_FOUND);
            }
        } else {
            log.warn(LogMessages.USER_WITH_USERNAME_NOT_FOUND, username);
            return new ResponseEntity<>("User with username: " + username + " does not exist!", HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<?> deleteJournalEntryFromUser(@PathVariable String journalId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        journalEntryService.deleteJournalEntryFromUser(journalId, username);
        log.info(LogMessages.DELETE_JOURNAL_BY_ID_AND_USER, journalId, username);
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
                log.info(LogMessages.UPDATE_JOURNAL_BY_ID_AND_USER, journalId, username);
                return new ResponseEntity<>(oldEntry, HttpStatus.ACCEPTED);
            } else {
                log.info(LogMessages.JOURNAL_NOT_FOUND_FOR_USER);
                return new ResponseEntity<>("Journal entry with ID: " + journalId + " does not exist for user with username: " + username + "!", HttpStatus.NOT_FOUND);
            }
        } else {
            log.warn(LogMessages.USER_WITH_USERNAME_NOT_FOUND, username);
            return new ResponseEntity<>("User with username: " + username + " does not exist!", HttpStatus.NOT_FOUND);
        }
    }
}
