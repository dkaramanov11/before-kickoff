package com.beforekickoff.backend.integration.ml;

public class MlPredictionUnavailableException extends RuntimeException {

    public MlPredictionUnavailableException(String message) {
        super(message);
    }

    public MlPredictionUnavailableException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}