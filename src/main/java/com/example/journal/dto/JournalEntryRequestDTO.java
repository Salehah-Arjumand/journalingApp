package com.example.journal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class JournalEntryRequestDTO {
    @NotBlank
    private String title;
    private String content;
    private LocalDateTime date;
}
