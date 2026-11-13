package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.integration.footballdata.FootballDataClient;
import com.beforekickoff.backend.integration.footballdata.dto.standings.FootballDataStanding;
import com.beforekickoff.backend.integration.footballdata.dto.standings.FootballDataStandingEntry;
import com.beforekickoff.backend.model.LeagueStanding;
import com.beforekickoff.backend.model.LeagueStandingEntry;
import com.beforekickoff.backend.model.Team;
import com.beforekickoff.backend.repository.LeagueStandingEntryRepository;
import com.beforekickoff.backend.repository.LeagueStandingRepository;
import com.beforekickoff.backend.repository.TeamRepository;
import com.beforekickoff.backend.service.LeagueStandingImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LeagueStandingImportServiceImpl
        implements LeagueStandingImportService {

    private final FootballDataClient footballDataClient;
    private final LeagueStandingRepository leagueStandingRepository;
    private final LeagueStandingEntryRepository entryRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public int importPremierLeagueStandings() {
        var response = footballDataClient.getPremierLeagueStandings();

        if (response.competition() == null
                || !"PL".equals(response.competition().code())
                || response.competition().name() == null
                || response.competition().name().isBlank()
                || response.season() == null
                || response.season().startDate() == null
                || response.standings() == null) {
            throw new IllegalStateException(
                    "Football Data API returned invalid standings metadata"
            );
        }

        List<FootballDataStanding> totalStandings =
                response.standings().stream()
                        .filter(standing -> standing != null
                                && "TOTAL".equals(standing.type()))
                        .toList();

        if (totalStandings.size() != 1) {
            throw new IllegalStateException(
                    "Expected exactly one TOTAL standing"
            );
        }

        List<FootballDataStandingEntry> table =
                totalStandings.getFirst().table();

        if (table == null || table.size() != 20) {
            throw new IllegalStateException(
                    "Expected 20 teams in Premier League standings"
            );
        }

        Set<Long> externalTeamIds = new HashSet<>();
        Set<Integer> positions = new HashSet<>();
        List<Team> teams = new ArrayList<>();

        // Validate all rows and resolve teams before replacing saved data.
        for (FootballDataStandingEntry entry : table) {
            validateEntry(entry);

            if (!externalTeamIds.add(entry.team().id())
                    || !positions.add(entry.position())) {
                throw new IllegalStateException(
                        "Standings contain duplicate teams or positions"
                );
            }

            Team team = teamRepository
                    .findByExternalApiId(entry.team().id())
                    .orElseThrow(() -> new IllegalStateException(
                            "Team with external API id "
                                    + entry.team().id()
                                    + " was not found. Import teams first."
                    ));

            teams.add(team);
        }

        Instant syncedAt = Instant.now();
        int seasonStartYear = response.season().startDate().getYear();
        String competitionName = response.competition().name();

        LeagueStanding standing = leagueStandingRepository
                .findByCompetitionCode("PL")
                .orElseGet(() -> new LeagueStanding(
                        "PL",
                        competitionName,
                        seasonStartYear,
                        syncedAt
                ));

        standing.updateDetails(
                competitionName,
                seasonStartYear,
                syncedAt
        );

        standing = leagueStandingRepository.saveAndFlush(standing);

        // This bulk DELETE runs before the new rows are inserted.
        entryRepository.deleteEntriesByStandingId(standing.getId());

        List<LeagueStandingEntry> entries = new ArrayList<>();

        for (int index = 0; index < table.size(); index++) {
            FootballDataStandingEntry source = table.get(index);

            entries.add(new LeagueStandingEntry(
                    standing,
                    teams.get(index),
                    source.position(),
                    source.playedGames(),
                    source.won(),
                    source.draw(),
                    source.lost(),
                    source.goalsFor(),
                    source.goalsAgainst(),
                    source.goalDifference(),
                    source.points()
            ));
        }

        entryRepository.saveAllAndFlush(entries);

        return entries.size();
    }

    private void validateEntry(FootballDataStandingEntry entry) {
        if (entry == null
                || entry.team() == null
                || entry.team().id() == null
                || entry.team().id() <= 0
                || entry.position() == null
                || entry.position() < 1
                || entry.position() > 20
                || isInvalidCount(entry.playedGames())
                || isInvalidCount(entry.won())
                || isInvalidCount(entry.draw())
                || isInvalidCount(entry.lost())
                || isInvalidCount(entry.goalsFor())
                || isInvalidCount(entry.goalsAgainst())
                || entry.goalDifference() == null
                || entry.points() == null) {
            throw new IllegalStateException(
                    "Football Data API returned an invalid standings entry"
            );
        }
    }

    private boolean isInvalidCount(Integer value) {
        return value == null || value < 0;
    }
}