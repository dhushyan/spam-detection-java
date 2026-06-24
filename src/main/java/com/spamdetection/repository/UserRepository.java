package com.spamdetection.repository;

import com.spamdetection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * USER REPOSITORY
 * Replaces Django ORM: User.objects.get(username=username)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // User.objects.get(username=username)
    Optional<User> findByUsername(String username);

    // User.objects.get(email=email)
    Optional<User> findByEmail(String email);

    // User.objects.filter(username=username).exists()
    boolean existsByUsername(String username);

    // User.objects.filter(email=email).exists()
    boolean existsByEmail(String email);
}
