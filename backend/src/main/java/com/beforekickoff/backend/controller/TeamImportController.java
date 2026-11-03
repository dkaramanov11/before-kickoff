package com.beforekickoff.backend.controller;

import com.beforekickoff.backend.dto.TeamImportResponse;
import com.beforekickoff.backend.service.TeamImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teams/import")
@RequiredArgsConstructor
public class TeamImportController {

    private final TeamImportService teamImportService;

    @PostMapping("/premier-league")
    public TeamImportResponse importPremierLeagueTeams() {
        int processedTeams = teamImportService.importPremierLeagueTeams();

        return new TeamImportResponse(processedTeams);
    }
}