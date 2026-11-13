package com.beforekickoff.backend.integration.footballdata.dto.standings;

import com.beforekickoff.backend.integration.footballdata.dto.team.FootballDataTeam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FootballDataStandingEntry(
        Integer position,
        FootballDataTeam team,
        Integer playedGames,
        Integer won,
        Integer draw,
        Integer lost,
        Integer goalsFor,
        Integer goalsAgainst,
        Integer goalDifference,
        Integer points
) {
}