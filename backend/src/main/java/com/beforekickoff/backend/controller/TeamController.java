package com.beforekickoff.backend.controller;

import com.beforekickoff.backend.dto.TeamResponse;
import com.beforekickoff.backend.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}