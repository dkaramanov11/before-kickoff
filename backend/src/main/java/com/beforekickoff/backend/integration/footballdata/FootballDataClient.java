package com.beforekickoff.backend.integration.footballdata;

import com.beforekickoff.backend.config.FootballDataProperties;
import com.beforekickoff.backend.integration.footballdata.dto.team.FootballDataTeam;
import com.beforekickoff.backend.integration.footballdata.dto.team.FootballDataTeamsResponse;
import com.beforekickoff.backend.integration.footballdata.dto.match.FootballDataMatch;
import com.beforekickoff.backend.integration.footballdata.dto.match.FootballDataMatchesResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class FootballDataClient {

    private final RestClient restClient;

    public FootballDataClient(FootballDataProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("X-Auth-Token", properties.apiToken())
                .build();
    }

    public List<FootballDataTeam> getPremierLeagueTeams() {
        FootballDataTeamsResponse response = restClient.get()
                .uri("/competitions/PL/teams")
                .retrieve()
                .body(FootballDataTeamsResponse.class);

        if (response == null || response.teams() == null) {
            throw new IllegalStateException(
                    "Football-data API returned an invalid teams response"
            );
        }

        return response.teams();
    }

    public List<FootballDataMatch> getPremierLeagueMatches() {
        FootballDataMatchesResponse response = restClient.get()
                .uri("/competitions/PL/matches")
                .retrieve()
                .body(FootballDataMatchesResponse.class);

        if (response == null || response.matches() == null) {
            throw new IllegalStateException(
                    "Football-data API returned an invalid matches response"
            );
        }

        return response.matches();
    }
}