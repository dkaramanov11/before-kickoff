package com.beforekickoff.backend.integration.footballdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FootballDataTeam(
        Long id,
        String name,
        String shortName,
        String crest
) {
}