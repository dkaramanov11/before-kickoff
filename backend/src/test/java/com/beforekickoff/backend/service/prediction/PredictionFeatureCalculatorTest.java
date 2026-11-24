package com.beforekickoff.backend.service.prediction;

import com.beforekickoff.backend.model.FootballMatch;
import com.beforekickoff.backend.model.Team;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PredictionFeatureCalculatorTest {

    private final PredictionFeatureCalculator calculator =
            new PredictionFeatureCalculator();

    @Test
    void calculatesFeaturesFromEachTeamsPerspective() {
        Team teamA = mock(Team.class);
        Team teamB = mock(Team.class);

        when(teamA.getId()).thenReturn(1L);
        when(teamB.getId()).thenReturn(2L);

        List<FootballMatch> history = List.of(
                match(teamA, teamB, 2, 0),
                match(teamB, teamA, 1, 1),
                match(teamA, teamB, 0, 3),
                match(teamB, teamA, 1, 4),
                match(teamA, teamB, 1, 1)
        );

        var result = calculator.calculate(
                1L, history,
                2L, history
        );

        assertAll(
                () -> assertEquals(8, result.homePointsLast5()),
                () -> assertEquals(5, result.awayPointsLast5()),
                () -> assertEquals(
                        1.6, result.homeGoalsForAvgLast5(), 1e-9
                ),
                () -> assertEquals(
                        1.2, result.awayGoalsForAvgLast5(), 1e-9
                ),
                () -> assertEquals(
                        1.2, result.homeGoalsAgainstAvgLast5(), 1e-9
                ),
                () -> assertEquals(
                        1.6, result.awayGoalsAgainstAvgLast5(), 1e-9
                )
        );
    }

    @Test
    void rejectsInsufficientHistory() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(
                        1L, List.of(),
                        2L, List.of()
                )
        );
    }

    private FootballMatch match(
            Team homeTeam,
            Team awayTeam,
            int homeScore,
            int awayScore
    ) {
        FootballMatch match = new FootballMatch(
                null,
                homeTeam,
                awayTeam,
                Instant.parse("2026-10-01T15:00:00Z"),
                "FINISHED",
                1,
                homeScore,
                awayScore
        );

        match.assignCompetition("PL");

        return match;
    }
}