package com.spamdetection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point — equivalent to Django's manage.py runserver
 * Run this to start the application on http://localhost:8080
 */
@SpringBootApplication
public class SpamDetectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpamDetectionApplication.class, args);
    }
}
