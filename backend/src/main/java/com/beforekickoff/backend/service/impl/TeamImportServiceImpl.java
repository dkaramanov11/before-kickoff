package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.integration.footballdata.FootballDataClient;
import com.beforekickoff.backend.integration.footballdata.dto.team.FootballDataTeam;
import com.beforekickoff.backend.model.Team;
import com.beforekickoff.backend.repository.TeamRepository;
import com.beforekickoff.backend.service.TeamImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamImportServiceImpl implements TeamImportService {

    private final FootballDataClient footballDataClient;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public int importPremierLeagueTeams() {
        List<FootballDataTeam> apiTeams =
                footballDataClient.getPremierLeagueTeams();

        for (FootballDataTeam apiTeam : apiTeams) {
            if (apiTeam.id() == null
                    || apiTeam.name() == null
                    || apiTeam.name().isBlank()) {
                throw new IllegalStateException(
                        "Football-data API returned a team without an ID or name"
                );
            }

            Team team = teamRepository
                    .findByExternalApiId(apiTeam.id())
                    .orElseGet(() -> new Team(
                            apiTeam.id(),
                            apiTeam.name(),
                            apiTeam.shortName(),
                            apiTeam.crest()
                    ));

            team.updateDetails(
                    apiTeam.name(),
                    apiTeam.shortName(),
                    apiTeam.crest()
            );

            teamRepository.save(team);
        }

        return apiTeams.size();
    }
}