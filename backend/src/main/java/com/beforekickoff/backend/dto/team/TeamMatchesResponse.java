package com.beforekickoff.backend.dto.team;

import com.beforekickoff.backend.dto.match.FootballMatchResponse;

import java.util.List;

public record TeamMatchesResponse(
        Long teamId,
        List<FootballMatchResponse> recentMatches,
        List<FootballMatchResponse> upcomingMatches
) {

    public TeamMatchesResponse {
        recentMatches = List.copyOf(recentMatches);
        upcomingMatches = List.copyOf(upcomingMatches);
    }
}