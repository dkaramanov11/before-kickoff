package com.beforekickoff.backend.dto.standings;

import java.time.Instant;
import java.util.List;

public record LeagueStandingResponse(
        String competitionCode,
        String competitionName,
        Integer seasonStartYear,
        Instant lastSyncedAt,
        List<LeagueStandingEntryResponse> standings
) {
}