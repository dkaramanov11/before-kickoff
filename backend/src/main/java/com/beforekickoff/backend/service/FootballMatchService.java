package com.beforekickoff.backend.service;

import com.beforekickoff.backend.dto.match.FootballMatchResponse;
import java.time.LocalDate;

import java.util.List;

public interface FootballMatchService {

    List<FootballMatchResponse> getAllMatches();

    List<FootballMatchResponse> getMatches(
            Long teamId,
            String status,
            LocalDate dateFrom,
            LocalDate dateTo
    );

    FootballMatchResponse getMatchById(Long id);

}