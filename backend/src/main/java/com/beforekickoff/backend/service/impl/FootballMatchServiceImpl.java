package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.dto.match.FootballMatchResponse;
import com.beforekickoff.backend.repository.FootballMatchRepository;
import com.beforekickoff.backend.service.FootballMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FootballMatchServiceImpl implements FootballMatchService {

    private final FootballMatchRepository footballMatchRepository;

    @Override
    public List<FootballMatchResponse> getAllMatches() {
        return footballMatchRepository
                .findAllByOrderByKickoffAtAscIdAsc()
                .stream()
                .map(FootballMatchResponse::from)
                .toList();
    }
}