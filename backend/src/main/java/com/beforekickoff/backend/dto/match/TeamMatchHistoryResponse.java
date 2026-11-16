package com.beforekickoff.backend.dto.match;

import java.util.List;

public record TeamMatchHistoryResponse(
        Long teamId,
        List<FootballMatchResponse> recentMatches
) {
}