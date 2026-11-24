package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.dto.prediction.MatchPredictionResponse;
import com.beforekickoff.backend.dto.prediction.PredictionProbabilitiesResponse;
import com.beforekickoff.backend.integration.ml.MlPredictionClient;
import com.beforekickoff.backend.integration.ml.MlPredictionUnavailableException;
import com.beforekickoff.backend.service.MatchPredictionService;
import com.beforekickoff.backend.service.prediction.PredictionFeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchPredictionServiceImpl implements MatchPredictionService {

    private final PredictionFeatureService featureService;
    private final MlPredictionClient mlPredictionClient;

    @Override
    public MatchPredictionResponse getPrediction(Long matchId) {
        var features = featureService.prepareFeatures(matchId);

        if (features.isEmpty()) {
            return MatchPredictionResponse.insufficientHistory(matchId);
        }

        try {
            var prediction = mlPredictionClient.predict(features.get());
            var probabilities = prediction.probabilities();

            return MatchPredictionResponse.success(
                    matchId,
                    prediction.modelVersion(),
                    prediction.predictedOutcome(),
                    new PredictionProbabilitiesResponse(
                            probabilities.get("1"),
                            probabilities.get("X"),
                            probabilities.get("2")
                    )
            );
        } catch (MlPredictionUnavailableException exception) {
            log.warn(
                    "Prediction unavailable for match {}",
                    matchId,
                    exception
            );

            return MatchPredictionResponse.unavailable(matchId);
        }
    }
}