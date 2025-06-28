package com.example.journal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
