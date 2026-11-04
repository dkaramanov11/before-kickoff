package com.beforekickoff.backend.integration.footballdata.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FootballDataMatchesResponse(
        List<FootballDataMatch> matches
) {
}