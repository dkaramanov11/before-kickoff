package com.beforekickoff.backend.dto.prediction;

public record PredictionProbabilitiesResponse(
        double homeWin,
        double draw,
        double awayWin
) {
}