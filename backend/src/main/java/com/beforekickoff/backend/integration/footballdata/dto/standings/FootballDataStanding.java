package com.beforekickoff.backend.integration.footballdata.dto.standings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FootballDataStanding(
        String type,
        List<FootballDataStandingEntry> table
) {
}