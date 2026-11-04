package com.beforekickoff.backend.dto.match;

import com.beforekickoff.backend.dto.team.TeamResponse;
import com.beforekickoff.backend.model.FootballMatch;

import java.time.Instant;

public record FootballMatchResponse(
        Long id,
        TeamResponse homeTeam,
        TeamResponse awayTeam,
        Instant kickoffAt,
        String status,
        Integer matchday,
        Integer homeScore,
        Integer awayScore
) {

    public static FootballMatchResponse from(FootballMatch match) {
        return new FootballMatchResponse(
                match.getId(),
                TeamResponse.from(match.getHomeTeam()),
                TeamResponse.from(match.getAwayTeam()),
                match.getKickoffAt(),
                match.getStatus(),
                match.getMatchday(),
                match.getHomeScore(),
                match.getAwayScore()
        );
    }
}