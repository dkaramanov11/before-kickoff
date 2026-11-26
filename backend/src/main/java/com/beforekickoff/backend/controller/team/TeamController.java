package com.beforekickoff.backend.controller.team;

import com.beforekickoff.backend.dto.team.TeamResponse;
import com.beforekickoff.backend.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.beforekickoff.backend.dto.team.TeamMatchesResponse;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public List<TeamResponse> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable("id") Long id) {
        return teamService.getTeamById(id);
    }

    @GetMapping("/{id}/matches")
    public TeamMatchesResponse getTeamMatches(
            @PathVariable("id") Long id
    ) {
        return teamService.getTeamMatches(id);
    }
}