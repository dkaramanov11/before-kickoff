package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.dto.match.FootballMatchResponse;
import com.beforekickoff.backend.repository.FootballMatchRepository;
import com.beforekickoff.backend.service.FootballMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.beforekickoff.backend.model.FootballMatch;
import com.beforekickoff.backend.repository.specification.FootballMatchSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FootballMatchServiceImpl implements FootballMatchService {

    private final FootballMatchRepository footballMatchRepository;
    private static final ZoneId APP_ZONE = ZoneId.of("Europe/Skopje");

    @Override
    public List<FootballMatchResponse> getAllMatches() {
        return footballMatchRepository
                .findAllByOrderByKickoffAtAscIdAsc()
                .stream()
                .map(FootballMatchResponse::from)
                .toList();
    }

    @Override
    public List<FootballMatchResponse> getMatches(
            Long teamId,
            String status,
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        if (teamId != null && teamId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "teamId must be a positive number"
            );
        }

        if (dateFrom != null
                && dateTo != null
                && dateFrom.isAfter(dateTo)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "dateFrom must be on or before dateTo"
            );
        }

        String normalizedStatus = status == null
                ? null
                : status.trim().toUpperCase(Locale.ROOT);

        Instant from = dateFrom == null
                ? null
                : dateFrom.atStartOfDay(APP_ZONE).toInstant();

        Instant toExclusive = dateTo == null
                ? null
                : dateTo.plusDays(1).atStartOfDay(APP_ZONE).toInstant();

        Specification<FootballMatch> specification =
                FootballMatchSpecifications.hasTeam(teamId)
                        .and(FootballMatchSpecifications.hasStatus(
                                normalizedStatus
                        ))
                        .and(FootballMatchSpecifications.kickoffFrom(from))
                        .and(FootballMatchSpecifications.kickoffBefore(
                                toExclusive
                        ));

        Sort sort = Sort.by("kickoffAt").ascending()
                .and(Sort.by("id").ascending());

        return footballMatchRepository.findAll(specification, sort)
                .stream()
                .map(FootballMatchResponse::from)
                .toList();
    }

    @Override
    public FootballMatchResponse getMatchById(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "id must be a positive number"
            );
        }

        FootballMatch match = footballMatchRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match with id " + id + " was not found"
                ));

        return FootballMatchResponse.from(match);
    }
}