package com.beforekickoff.backend.controller.match;

import com.beforekickoff.backend.dto.match.FootballMatchResponse;
import com.beforekickoff.backend.service.FootballMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.beforekickoff.backend.dto.match.FootballMatchHistoryResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class FootballMatchController {

    private final FootballMatchService footballMatchService;

    @GetMapping
    public List<FootballMatchResponse> getMatches(
            @RequestParam(name = "teamId", required = false)
            Long teamId,

            @RequestParam(name = "status", required = false)
            String status,

            @RequestParam(name = "dateFrom", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFrom,

            @RequestParam(name = "dateTo", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateTo
    ) {
        return footballMatchService.getMatches(
                teamId,
                status,
                dateFrom,
                dateTo
        );
    }

    @GetMapping("/{id}")
    public FootballMatchResponse getMatchById(@PathVariable("id") Long id) {
        return footballMatchService.getMatchById(id);
    }

    @GetMapping("/{id}/history")
    public FootballMatchHistoryResponse getMatchHistory(
            @PathVariable("id") Long id
    ) {
        return footballMatchService.getMatchHistory(id);
    }
}