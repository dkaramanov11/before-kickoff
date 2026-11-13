package com.beforekickoff.backend.dto.standings;

import com.beforekickoff.backend.dto.team.TeamResponse;
import com.beforekickoff.backend.model.LeagueStandingEntry;

public record LeagueStandingEntryResponse(
        Integer position,
        TeamResponse team,
        Integer playedGames,
        Integer won,
        Integer draw,
        Integer lost,
        Integer goalsFor,
        Integer goalsAgainst,
        Integer goalDifference,
        Integer points
) {

    public static LeagueStandingEntryResponse from(
            LeagueStandingEntry entry
    ) {
        return new LeagueStandingEntryResponse(
                entry.getPosition(),
                TeamResponse.from(entry.getTeam()),
                entry.getPlayedGames(),
                entry.getWon(),
                entry.getDraw(),
                entry.getLost(),
                entry.getGoalsFor(),
                entry.getGoalsAgainst(),
                entry.getGoalDifference(),
                entry.getPoints()
        );
    }
}