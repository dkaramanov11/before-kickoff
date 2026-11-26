package com.beforekickoff.backend.service;

import com.beforekickoff.backend.dto.team.TeamResponse;
import com.beforekickoff.backend.dto.team.TeamMatchesResponse;

import java.util.List;

public interface TeamService {

    List<TeamResponse> getAllTeams();

    TeamResponse getTeamById(Long id);

    TeamMatchesResponse getTeamMatches(Long id);    
}