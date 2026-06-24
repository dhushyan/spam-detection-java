package com.spamdetection.service;

import com.spamdetection.dto.Dtos.*;
import com.spamdetection.model.Email;
import com.spamdetection.model.User;
import com.spamdetection.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * EMAIL SERVICE
 * Replaces Django's views.py business logic
 * Controllers call this, this calls Repository + MlService
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;
    private final MlService mlService;

    /**
     * Check if email is spam — main feature of your project
     * Replaces Django view: def check_email(request)
     */
    public Email checkEmail(String content, User user) {
        // 1. Call Python ML model
        MlPredictionResponse mlResult = mlService.predict(content);

        // 2. Save result to MySQL
        Email email = new Email();
        email.setContent(content);
        email.setPrediction(mlResult.getPrediction());
        email.setConfidence(mlResult.getConfidence());
        email.setUser(user);

        return emailRepository.save(email);
    }

    /**
     * Get all emails for a specific user (for dashboard/history)
     * Replaces: Email.objects.filter(user=request.user)
     */
    public List<Email> getUserEmails(User user) {
        return emailRepository.findByUserOrderByCheckedAtDesc(user);
    }

    /**
     * Get all emails (admin view)
     * Replaces: Email.objects.all()
     */
    public List<Email> getAllEmails() {
        return emailRepository.findAllByOrderByCheckedAtDesc();
    }

    /**
     * Delete an email record
     */
    public void deleteEmail(Long id) {
        emailRepository.deleteById(id);
    }

    /**
     * Dashboard statistics
     * Replaces: Email.objects.filter(prediction='spam').count()
     */
    public DashboardStats getStats(User user) {
        long total = emailRepository.countByUser(user);
        long spam  = emailRepository.countByUserAndPrediction(user, "spam");
        long ham   = total - spam;
        double spamPct = total > 0 ? (spam * 100.0 / total) : 0;

        DashboardStats stats = new DashboardStats();
        stats.setTotalChecked(total);
        stats.setSpamCount(spam);
        stats.setHamCount(ham);
        stats.setSpamPercentage(spamPct);
        return stats;
    }
}
