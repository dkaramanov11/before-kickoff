package com.beforekickoff.backend.integration.ml;

import com.beforekickoff.backend.config.MlProperties;
import com.beforekickoff.backend.integration.ml.dto.MlPredictionRequest;
import com.beforekickoff.backend.integration.ml.dto.MlPredictionResponse;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.Map;
import java.util.Set;

@Component
public class MlPredictionClient {

    private static final Set<String> OUTCOMES =
            Set.of("1", "X", "2");

    private static final double SUM_TOLERANCE = 1e-6;
    private static final double OUTCOME_TOLERANCE = 1e-9;

    private final RestClient restClient;

    public MlPredictionClient(MlProperties properties) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(
                        Duration.ofMillis(properties.connectTimeoutMs())
                )
                .build();

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory(httpClient);

        requestFactory.setReadTimeout(
                Duration.ofMillis(properties.readTimeoutMs())
        );

        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .requestFactory(requestFactory)
                .build();
    }

    public MlPredictionResponse predict(MlPredictionRequest request) {
        MlPredictionResponse response;

        try {
            response = restClient.post()
                    .uri("/predict")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(MlPredictionResponse.class);
        } catch (RestClientException exception) {
            throw new MlPredictionUnavailableException(
                    "ML prediction request failed",
                    exception
            );
        }

        validateResponse(response);

        return response;
    }

    private void validateResponse(MlPredictionResponse response) {
        if (response == null
                || !"ok".equals(response.status())
                || response.modelVersion() == null
                || response.modelVersion().isBlank()
                || response.predictedOutcome() == null
                || !OUTCOMES.contains(response.predictedOutcome())) {
            throw invalidResponse();
        }

        Map<String, Double> probabilities = response.probabilities();

        if (probabilities == null
                || !probabilities.keySet().equals(OUTCOMES)) {
            throw invalidResponse();
        }

        double sum = 0;
        double maximum = 0;

        for (String outcome : OUTCOMES) {
            Double probability = probabilities.get(outcome);

            if (probability == null
                    || !Double.isFinite(probability)
                    || probability < 0
                    || probability > 1) {
                throw invalidResponse();
            }

            sum += probability;
            maximum = Math.max(maximum, probability);
        }

        if (Math.abs(sum - 1.0) > SUM_TOLERANCE) {
            throw invalidResponse();
        }

        double predictedProbability =
                probabilities.get(response.predictedOutcome());

        if (maximum - predictedProbability > OUTCOME_TOLERANCE) {
            throw invalidResponse();
        }
    }

    private MlPredictionUnavailableException invalidResponse() {
        return new MlPredictionUnavailableException(
                "ML service returned an invalid prediction response"
        );
    }
}