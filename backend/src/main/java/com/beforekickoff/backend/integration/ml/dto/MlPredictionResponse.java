package com.beforekickoff.backend.integration.ml.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MlPredictionResponse(
        String status,

        @JsonProperty("model_version")
        String modelVersion,

        @JsonProperty("predicted_outcome")
        String predictedOutcome,

        Map<String, Double> probabilities
) {
}