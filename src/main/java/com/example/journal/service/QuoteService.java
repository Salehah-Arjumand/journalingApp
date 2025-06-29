package com.example.journal.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class QuoteService {

    private final RestTemplate restTemplate;

    public QuoteService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String getRandomQuote() {
        String url = "https://zenquotes.io/api/random";
        Map[] response = restTemplate.getForObject(url, Map[].class);
        if (response != null && response.length > 0) {
            return response[0].get("q") + " — " + response[0].get("a");
        }
        return "No quote available.";
    }
}
