package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.dto.team.TeamResponse;
import com.beforekickoff.backend.repository.TeamRepository;
import com.beforekickoff.backend.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public TeamResponse getTeamById(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "id must be a positive number"
            );
        }

        return teamRepository.findById(id)
                .map(TeamResponse::from)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Team with id " + id + " was not found"
                ));
    }
}