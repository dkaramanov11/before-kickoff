package com.beforekickoff.backend.service;

import com.beforekickoff.backend.dto.TeamResponse;

import java.util.List;

public interface TeamService {

    List<TeamResponse> getAllTeams();
}