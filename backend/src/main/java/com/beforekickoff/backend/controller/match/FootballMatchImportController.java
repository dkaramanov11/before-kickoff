package com.beforekickoff.backend.controller.match;

import com.beforekickoff.backend.dto.match.FootballMatchImportResponse;
import com.beforekickoff.backend.service.FootballMatchImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches/import")
@RequiredArgsConstructor
public class FootballMatchImportController {

    private final FootballMatchImportService footballMatchImportService;

    @PostMapping("/premier-league")
    public FootballMatchImportResponse importPremierLeagueMatches() {
        int processedMatches =
                footballMatchImportService.importPremierLeagueMatches();

        return new FootballMatchImportResponse(processedMatches);
    }
}