package com.beforekickoff.backend.dto.prediction;

public record MatchPredictionResponse(
        Long matchId,
        String status,
        String modelVersion,
        String predictedOutcome,
        PredictionProbabilitiesResponse probabilities
) {

    public static MatchPredictionResponse success(
            Long matchId,
            String modelVersion,
            String predictedOutcome,
            PredictionProbabilitiesResponse probabilities
    ) {
        return new MatchPredictionResponse(
                matchId,
                "ok",
                modelVersion,
                predictedOutcome,
                probabilities
        );
    }

    public static MatchPredictionResponse insufficientHistory(Long matchId) {
        return new MatchPredictionResponse(
                matchId,
                "insufficient_history",
                null,
                null,
                null
        );
    }

    public static MatchPredictionResponse unavailable(Long matchId) {
        return new MatchPredictionResponse(
                matchId,
                "prediction_unavailable",
                null,
                null,
                null
        );
    }
}