package com.beforekickoff.backend.integration.footballdata.dto.standings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FootballDataStandingsResponse(
        Competition competition,
        Season season,
        List<FootballDataStanding> standings
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Competition(
            String code,
            String name
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Season(
            LocalDate startDate
    ) {
    }
}