package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.integration.footballdata.FootballDataClient;
import com.beforekickoff.backend.integration.footballdata.dto.match.FootballDataMatch;
import com.beforekickoff.backend.model.FootballMatch;
import com.beforekickoff.backend.model.Team;
import com.beforekickoff.backend.repository.FootballMatchRepository;
import com.beforekickoff.backend.repository.TeamRepository;
import com.beforekickoff.backend.service.FootballMatchImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FootballMatchImportServiceImpl
        implements FootballMatchImportService {

    private final FootballDataClient footballDataClient;
    private final FootballMatchRepository footballMatchRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public int importPremierLeagueMatches() {
        List<FootballDataMatch> apiMatches =
                footballDataClient.getPremierLeagueMatches();

        for (FootballDataMatch apiMatch : apiMatches) {
            validateMatch(apiMatch);

            Team homeTeam = findTeam(apiMatch.homeTeam().id());
            Team awayTeam = findTeam(apiMatch.awayTeam().id());

            Integer homeScore = null;
            Integer awayScore = null;

            if (apiMatch.score() != null
                    && apiMatch.score().fullTime() != null) {
                homeScore = apiMatch.score().fullTime().home();
                awayScore = apiMatch.score().fullTime().away();
            }

            FootballMatch match = footballMatchRepository
                    .findByExternalApiId(apiMatch.id())
                    .orElse(null);

            if (match == null) {
                match = new FootballMatch(
                        apiMatch.id(),
                        homeTeam,
                        awayTeam,
                        apiMatch.utcDate(),
                        apiMatch.status(),
                        apiMatch.matchday(),
                        homeScore,
                        awayScore
                );
            } else {
                match.updateDetails(
                        homeTeam,
                        awayTeam,
                        apiMatch.utcDate(),
                        apiMatch.status(),
                        apiMatch.matchday(),
                        homeScore,
                        awayScore
                );
            }

            match.assignCompetition("PL");

            footballMatchRepository.save(match);
        }

        return apiMatches.size();
    }

    private Team findTeam(Long externalApiId) {
        return teamRepository.findByExternalApiId(externalApiId)
                .orElseThrow(() -> new IllegalStateException(
                        "Team with external API ID " + externalApiId
                                + " was not found. Import teams first."
                ));
    }

    private void validateMatch(FootballDataMatch match) {
        if (match == null
                || match.id() == null
                || match.utcDate() == null
                || match.status() == null
                || match.status().isBlank()
                || match.homeTeam() == null
                || match.homeTeam().id() == null
                || match.awayTeam() == null
                || match.awayTeam().id() == null) {
            throw new IllegalStateException(
                    "Football-data API returned an invalid match"
            );
        }

        if (match.homeTeam().id().equals(match.awayTeam().id())) {
            throw new IllegalStateException(
                    "A match cannot have the same home and away team"
            );
        }
    }
}