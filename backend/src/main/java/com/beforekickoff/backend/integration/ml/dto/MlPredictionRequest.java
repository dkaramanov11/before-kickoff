package com.beforekickoff.backend.integration.ml.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MlPredictionRequest(

        @JsonProperty("home_points_last5")
        int homePointsLast5,

        @JsonProperty("away_points_last5")
        int awayPointsLast5,

        @JsonProperty("home_goals_for_avg_last5")
        double homeGoalsForAvgLast5,

        @JsonProperty("away_goals_for_avg_last5")
        double awayGoalsForAvgLast5,

        @JsonProperty("home_goals_against_avg_last5")
        double homeGoalsAgainstAvgLast5,

        @JsonProperty("away_goals_against_avg_last5")
        double awayGoalsAgainstAvgLast5
) {
}