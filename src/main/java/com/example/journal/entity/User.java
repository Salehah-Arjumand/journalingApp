package com.example.journal.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    private String id;
    @NonNull
    @Indexed(unique = true)
    private String username;
    @NonNull
    private String password;
    private List<RolesEnum> roles;
    @DBRef
    private List<JournalEntry> journalEntries = new ArrayList<>();

    public User(String id, @NonNull String username, @NonNull String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public JournalEntry getJournalEntryById(String id){
        for (JournalEntry journalEntry : journalEntries) {
            if (journalEntry.getId().equals(id)) {
                return journalEntry;
            }
        }
        return null;
    }
}
