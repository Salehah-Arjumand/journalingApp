package com.example.journal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheck {
    @GetMapping("/health-check")
    public String healthCheck() {
        log.info("Calling health check");
        return "Ok";
    }
}
