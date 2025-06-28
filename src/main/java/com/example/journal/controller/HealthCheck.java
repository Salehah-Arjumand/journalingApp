package com.example.journal.controller;

import com.example.journal.utils.LogMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheck {
    @GetMapping("/health-check")
    public String healthCheck() {
        log.info(LogMessages.HEALTH_CHECK);
        return "Ok";
    }
}
