package com.spamdetection.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * EMAIL MODEL
 * Replaces Django's models.py Email model
 * JPA auto-creates the MySQL table (like Django migrations)
 */
@Entity
@Table(name = "emails")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The raw email text entered by user
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    // "spam" or "ham" — result from Python ML model
    @Column(nullable = false)
    private String prediction;

    // Confidence score from ML model (0.0 to 1.0)
    @Column
    private Double confidence;

    // Who submitted this (linked to User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Auto timestamp — like Django's auto_now_add
    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @PrePersist
    public void prePersist() {
        this.checkedAt = LocalDateTime.now();
    }
}
