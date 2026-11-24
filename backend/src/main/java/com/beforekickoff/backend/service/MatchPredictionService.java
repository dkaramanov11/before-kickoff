package com.beforekickoff.backend.service;

import com.beforekickoff.backend.dto.prediction.MatchPredictionResponse;

public interface MatchPredictionService {

    MatchPredictionResponse getPrediction(Long matchId);
}