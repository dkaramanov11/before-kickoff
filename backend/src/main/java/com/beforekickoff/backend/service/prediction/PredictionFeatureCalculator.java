package com.beforekickoff.backend.service.prediction;

import com.beforekickoff.backend.integration.ml.dto.MlPredictionRequest;
import com.beforekickoff.backend.model.FootballMatch;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class PredictionFeatureCalculator {

    private static final int REQUIRED_MATCHES = 5;

    public MlPredictionRequest calculate(
            Long homeTeamId,
            List<FootballMatch> homeHistory,
            Long awayTeamId,
            List<FootballMatch> awayHistory
    ) {
        TeamFeatures home = calculateTeamFeatures(
                homeTeamId,
                homeHistory
        );

        TeamFeatures away = calculateTeamFeatures(
                awayTeamId,
                awayHistory
        );

        return new MlPredictionRequest(
                home.pointsLast5(),
                away.pointsLast5(),
                home.goalsForAvgLast5(),
                away.goalsForAvgLast5(),
                home.goalsAgainstAvgLast5(),
                away.goalsAgainstAvgLast5()
        );
    }

    private TeamFeatures calculateTeamFeatures(
            Long teamId,
            List<FootballMatch> history
    ) {
        if (teamId == null
                || history == null
                || history.size() != REQUIRED_MATCHES) {
            throw new IllegalArgumentException(
                    "Exactly five matches are required for each team"
            );
        }

        int points = 0;
        long goalsFor = 0;
        long goalsAgainst = 0;

        for (FootballMatch match : history) {
            if (match == null
                    || !"FINISHED".equals(match.getStatus())
                    || !"PL".equals(match.getCompetitionCode())
                    || match.getHomeTeam() == null
                    || match.getAwayTeam() == null
                    || match.getHomeScore() == null
                    || match.getAwayScore() == null
                    || match.getHomeScore() < 0
                    || match.getAwayScore() < 0) {
                throw new IllegalArgumentException(
                        "History contains an invalid match"
                );
            }

            boolean isHomeTeam = Objects.equals(
                    teamId,
                    match.getHomeTeam().getId()
            );

            boolean isAwayTeam = Objects.equals(
                    teamId,
                    match.getAwayTeam().getId()
            );

            if (isHomeTeam == isAwayTeam) {
                throw new IllegalArgumentException(
                        "Team must appear on exactly one side of the match"
                );
            }

            int scored = isHomeTeam
                    ? match.getHomeScore()
                    : match.getAwayScore();

            int conceded = isHomeTeam
                    ? match.getAwayScore()
                    : match.getHomeScore();

            goalsFor += scored;
            goalsAgainst += conceded;

            if (scored > conceded) {
                points += 3;
            } else if (scored == conceded) {
                points += 1;
            }
        }

        return new TeamFeatures(
                points,
                goalsFor / (double) REQUIRED_MATCHES,
                goalsAgainst / (double) REQUIRED_MATCHES
        );
    }

    private record TeamFeatures(
            int pointsLast5,
            double goalsForAvgLast5,
            double goalsAgainstAvgLast5
    ) {
    }
}