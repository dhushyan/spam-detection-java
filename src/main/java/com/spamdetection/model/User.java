package com.spamdetection.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * USER MODEL
 * Replaces Django's built-in User model (auth_user table)
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username ;

    @Column(unique = true, nullable = false)
    private String email;

    // Stored as BCrypt hash — like Django's password hashing
    @Column(nullable = false)
    private String password;

    // ROLE_USER or ROLE_ADMIN (like Django's is_staff)
    @Column(nullable = false)
    private String role = "ROLE_USER";

    @Column(nullable = false)
    private boolean enabled = true;

    // One user → many email checks
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Email> emails;
}
