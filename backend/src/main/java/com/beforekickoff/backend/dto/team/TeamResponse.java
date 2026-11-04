package com.beforekickoff.backend.dto.team;

import com.beforekickoff.backend.model.Team;

public record TeamResponse(
        Long id,
        String name,
        String shortName,
        String crestUrl
) {

    public static TeamResponse from(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getShortName(),
                team.getCrestUrl()
        );
    }
}