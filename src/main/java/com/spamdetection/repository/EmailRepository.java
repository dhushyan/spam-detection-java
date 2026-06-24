package com.spamdetection.repository;

import com.spamdetection.model.Email;
import com.spamdetection.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EMAIL REPOSITORY
 * Replaces Django ORM queries like:
 *   Email.objects.filter(user=user)
 *   Email.objects.all()
 *   Email.objects.filter(prediction='spam').count()
 */
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    // Email.objects.filter(user=user).order_by('-checked_at')
    List<Email> findByUserOrderByCheckedAtDesc(User user);

    // Email.objects.all().order_by('-checked_at')
    List<Email> findAllByOrderByCheckedAtDesc();

    // Email.objects.filter(prediction='spam').count()
    long countByPrediction(String prediction);

    // Email.objects.filter(user=user, prediction='spam').count()
    long countByUserAndPrediction(User user, String prediction);

    // Custom query for dashboard stats
    @Query("SELECT COUNT(e) FROM Email e WHERE e.user = ?1")
    long countByUser(User user);
}
