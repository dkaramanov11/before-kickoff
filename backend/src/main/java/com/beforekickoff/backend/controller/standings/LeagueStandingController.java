package com.beforekickoff.backend.controller.standings;

import com.beforekickoff.backend.dto.standings.LeagueStandingResponse;
import com.beforekickoff.backend.service.LeagueStandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
public class LeagueStandingController {

    private final LeagueStandingService leagueStandingService;

    @GetMapping("/premier-league")
    public LeagueStandingResponse getPremierLeagueStandings() {
        return leagueStandingService.getPremierLeagueStandings();
    }
}