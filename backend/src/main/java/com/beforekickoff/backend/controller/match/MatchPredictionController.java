package com.beforekickoff.backend.controller.match;

import com.beforekickoff.backend.dto.prediction.MatchPredictionResponse;
import com.beforekickoff.backend.service.MatchPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchPredictionController {

    private final MatchPredictionService predictionService;

    @GetMapping("/{id}/prediction")
    public ResponseEntity<MatchPredictionResponse> getPrediction(
            @PathVariable("id") Long id
    ) {
        MatchPredictionResponse response =
                predictionService.getPrediction(id);

        if ("prediction_unavailable".equals(response.status())) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }
}