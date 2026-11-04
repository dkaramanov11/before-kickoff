package com.beforekickoff.backend.service;

import com.beforekickoff.backend.dto.team.TeamResponse;

import java.util.List;

public interface TeamService {

    List<TeamResponse> getAllTeams();
}