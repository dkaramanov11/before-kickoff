package com.beforekickoff.backend.integration.footballdata.dto.match;

import com.beforekickoff.backend.integration.footballdata.dto.team.FootballDataTeam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FootballDataMatch(
        Long id,
        Instant utcDate,
        String status,
        Integer matchday,
        FootballDataTeam homeTeam,
        FootballDataTeam awayTeam,
        Score score
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Score(
            FullTime fullTime
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FullTime(
            Integer home,
            Integer away
    ) {
    }
}