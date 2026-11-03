package com.beforekickoff.backend.integration.footballdata;

import com.beforekickoff.backend.config.FootballDataProperties;
import com.beforekickoff.backend.integration.footballdata.dto.FootballDataTeam;
import com.beforekickoff.backend.integration.footballdata.dto.FootballDataTeamsResponse;
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
}