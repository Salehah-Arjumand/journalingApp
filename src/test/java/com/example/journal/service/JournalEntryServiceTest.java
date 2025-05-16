package com.example.journal.service;

import com.example.journal.entity.JournalEntry;
import com.example.journal.entity.User;
import com.example.journal.repository.JournalEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JournalEntryServiceTest {

    @Mock
    private JournalEntryRepository journalEntryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private JournalEntryService journalEntryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveEntry() {
        JournalEntry entry = new JournalEntry();
        journalEntryService.saveEntry(entry);
        verify(journalEntryRepository, times(1)).save(entry);
    }

    @Test
    public void testGetAllJournalEntries() {
        when(journalEntryRepository.findAll()).thenReturn(List.of(new JournalEntry(), new JournalEntry()));

        List<JournalEntry> entries = journalEntryService.getAllJournalEntries();
        assertEquals(2, entries.size());
    }

    @Test
    public void testFindJournalEntryById() {
        JournalEntry entry = new JournalEntry();
        entry.setId("abc123");
        when(journalEntryRepository.findById("abc123")).thenReturn(Optional.of(entry));

        JournalEntry found = journalEntryService.findJournalEntryById("abc123");
        assertNotNull(found);
        assertEquals("abc123", found.getId());
    }

    @Test
    public void testDeleteJournalEntryFromUser() {
        User user = new User();
        user.setUsername("john");
        JournalEntry entry = new JournalEntry();
        entry.setId("j1");
        List<JournalEntry> entries = new ArrayList<>();
        entries.add(entry);
        user.setJournalEntries(entries);

        when(userService.findUserByUsername("john")).thenReturn(user);

        journalEntryService.deleteJournalEntryFromUser("j1", "john");

        assertEquals(0, user.getJournalEntries().size());
        verify(userService, times(1)).saveUser(user);
        verify(journalEntryRepository, times(1)).deleteById("j1");
    }
}
