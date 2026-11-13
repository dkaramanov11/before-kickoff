package com.beforekickoff.backend.controller.standings;

import com.beforekickoff.backend.dto.standings.LeagueStandingImportResponse;
import com.beforekickoff.backend.service.LeagueStandingImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
public class LeagueStandingImportController {

    private final LeagueStandingImportService importService;

    @PostMapping("/import/premier-league")
    public LeagueStandingImportResponse importPremierLeagueStandings() {
        int processedTeams = importService.importPremierLeagueStandings();

        return new LeagueStandingImportResponse(processedTeams);
    }
}