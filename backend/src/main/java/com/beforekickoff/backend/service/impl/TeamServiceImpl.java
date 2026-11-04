package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.dto.team.TeamResponse;
import com.beforekickoff.backend.repository.TeamRepository;
import com.beforekickoff.backend.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    @Override
    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll(Sort.by("name").ascending())
                .stream()
                .map(TeamResponse::from)
                .toList();
    }
}