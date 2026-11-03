package com.beforekickoff.backend.service;

import com.beforekickoff.backend.dto.FootballMatchResponse;

import java.util.List;

public interface FootballMatchService {

    List<FootballMatchResponse> getAllMatches();
}