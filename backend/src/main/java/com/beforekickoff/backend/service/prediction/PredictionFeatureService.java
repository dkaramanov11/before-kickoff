package com.beforekickoff.backend.service.prediction;

import com.beforekickoff.backend.integration.ml.dto.MlPredictionRequest;
import com.beforekickoff.backend.model.FootballMatch;
import com.beforekickoff.backend.repository.FootballMatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PredictionFeatureService {

    private final FootballMatchRepository footballMatchRepository;
    private final PredictionFeatureCalculator featureCalculator;

    @Transactional(
            readOnly = true,
            isolation = Isolation.REPEATABLE_READ
    )
    public Optional<MlPredictionRequest> prepareFeatures(Long matchId) {
        if (matchId == null || matchId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "id must be a positive number"
            );
        }

        FootballMatch match = footballMatchRepository.findById(matchId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Match with id " + matchId + " was not found"
                ));

        if (!"PL".equals(match.getCompetitionCode())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Predictions are supported only for Premier League matches"
            );
        }

        LocalDate matchDate = match.getKickoffAt()
                .atZone(ZoneOffset.UTC)
                .toLocalDate();

        Instant fromInclusive = matchDate.minusDays(365)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Instant toExclusive = matchDate
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant();

        Instant now = Instant.now();
        PageRequest limit = PageRequest.of(0, 5);

        Long homeTeamId = match.getHomeTeam().getId();
        Long awayTeamId = match.getAwayTeam().getId();

        var homeHistory = footballMatchRepository.findPredictionHistory(
                homeTeamId,
                fromInclusive,
                toExclusive,
                now,
                limit
        );

        var awayHistory = footballMatchRepository.findPredictionHistory(
                awayTeamId,
                fromInclusive,
                toExclusive,
                now,
                limit
        );

        if (homeHistory.size() < 5 || awayHistory.size() < 5) {
            return Optional.empty();
        }

        return Optional.of(featureCalculator.calculate(
                homeTeamId,
                homeHistory,
                awayTeamId,
                awayHistory
        ));
    }
}