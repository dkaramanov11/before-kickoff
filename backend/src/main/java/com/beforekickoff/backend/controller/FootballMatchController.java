package com.beforekickoff.backend.controller;

import com.beforekickoff.backend.dto.FootballMatchResponse;
import com.beforekickoff.backend.service.FootballMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class FootballMatchController {

    private final FootballMatchService footballMatchService;

    @GetMapping
    public List<FootballMatchResponse> getAllMatches() {
        return footballMatchService.getAllMatches();
    }
}