package com.beforekickoff.backend.dto.match;

import java.time.Instant;
import java.util.List;

public record FootballMatchHistoryResponse(
        Long matchId,
        Instant referenceKickoffAt,
        TeamMatchHistoryResponse homeTeam,
        TeamMatchHistoryResponse awayTeam,
        List<FootballMatchResponse> headToHead
) {
}