package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.dto.standings.LeagueStandingEntryResponse;
import com.beforekickoff.backend.dto.standings.LeagueStandingResponse;
import com.beforekickoff.backend.model.LeagueStanding;
import com.beforekickoff.backend.repository.LeagueStandingEntryRepository;
import com.beforekickoff.backend.repository.LeagueStandingRepository;
import com.beforekickoff.backend.service.LeagueStandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class LeagueStandingServiceImpl implements LeagueStandingService {

    private final LeagueStandingRepository leagueStandingRepository;
    private final LeagueStandingEntryRepository entryRepository;

    @Override
    @Transactional(
            readOnly = true,
            isolation = Isolation.REPEATABLE_READ
    )
    public LeagueStandingResponse getPremierLeagueStandings() {
        LeagueStanding standing = leagueStandingRepository
                .findByCompetitionCode("PL")
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Premier League standings have not been imported yet"
                ));

        var entries = entryRepository
                .findAllByLeagueStanding_IdOrderByPositionAsc(
                        standing.getId()
                )
                .stream()
                .map(LeagueStandingEntryResponse::from)
                .toList();

        return new LeagueStandingResponse(
                standing.getCompetitionCode(),
                standing.getCompetitionName(),
                standing.getSeasonStartYear(),
                standing.getLastSyncedAt(),
                entries
        );
    }
}