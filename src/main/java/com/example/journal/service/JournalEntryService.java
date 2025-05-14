package com.example.journal.service;

import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.repository.JournalEntryRepository;
import com.example.journal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void saveJournalEntry(String username, JournalEntry journalEntry){
        try {
            User user = userService.findUserByUsername(username);

            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry");
        }
    }

    public void saveEntry(JournalEntry journalEntry) {
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry findJournalEntryById(String journalId) {
        return journalEntryRepository.findById(journalId).orElse(null);
    }

    @Transactional
    public void deleteJournalEntryFromUser(String journalId, String username) {
        User user = userService.findUserByUsername(username);
        user.getJournalEntries().removeIf(x -> x.getId().equals(journalId));
        userService.saveUser(user);
        journalEntryRepository.deleteById(journalId);

    }
}
