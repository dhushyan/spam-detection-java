package com.spamdetection.service;

import com.spamdetection.dto.Dtos.MlPredictionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;
import java.util.HashMap;

/**
 * ML SERVICE
 * Replaces Django's ml_model.py
 * Instead of loading model.pkl directly, this calls your Python Flask API
 * which still uses the original sklearn model — no retraining needed!
 *
 * Flow: Java sends email text → Python Flask → loads model.pkl → returns prediction
 */
@Service
public class MlService {

    @Value("${ml.api.url}")
    private String mlApiUrl;  // http://localhost:5000

    private final RestTemplate restTemplate;

    public MlService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Calls Python ML API to predict if email is spam or ham
     * Replaces: prediction = model.predict(vectorizer.transform([text]))
     */
    public MlPredictionResponse predict(String emailContent) {
        try {
            String url = mlApiUrl + "/predict";

            // Build request body: {"text": "email content here"}
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("text", emailContent);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

            // POST to Python Flask API
            ResponseEntity<MlPredictionResponse> response = restTemplate.postForEntity(
                url, request, MlPredictionResponse.class
            );

            return response.getBody();

        } catch (Exception e) {
            // If Python API is down, return error response
            MlPredictionResponse errorResponse = new MlPredictionResponse();
            errorResponse.setPrediction("error");
            errorResponse.setConfidence(0.0);
            errorResponse.setMessage("ML service unavailable: " + e.getMessage());
            return errorResponse;
        }
    }
}
