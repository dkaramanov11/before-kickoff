package com.beforekickoff.backend.repository;

import com.beforekickoff.backend.model.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

import java.util.List;

import java.util.Optional;

public interface FootballMatchRepository
        extends JpaRepository<FootballMatch, Long>,
        JpaSpecificationExecutor<FootballMatch> {

    Optional<FootballMatch> findByExternalApiId(Long externalApiId);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    List<FootballMatch> findAllByOrderByKickoffAtAscIdAsc();

    @Override
    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    List<FootballMatch> findAll(
            Specification<FootballMatch> specification,
            Sort sort
    );

    @Override
    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    Optional<FootballMatch> findById(Long id);

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    @Query("""
        SELECT m
        FROM FootballMatch m
        WHERE m.competitionCode = :competitionCode
          AND m.id <> :excludedMatchId
          AND m.status = 'FINISHED'
          AND m.homeScore IS NOT NULL
          AND m.awayScore IS NOT NULL
          AND m.kickoffAt < :referenceKickoffAt
          AND m.kickoffAt < :now
          AND (
              m.homeTeam.id = :teamId
              OR m.awayTeam.id = :teamId
          )
        ORDER BY m.kickoffAt DESC, m.id DESC
        """)
    List<FootballMatch> findRecentTeamMatches(
            @Param("competitionCode") String competitionCode,
            @Param("excludedMatchId") Long excludedMatchId,
            @Param("teamId") Long teamId,
            @Param("referenceKickoffAt") Instant referenceKickoffAt,
            @Param("now") Instant now,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"homeTeam", "awayTeam"})
    @Query("""
        SELECT m
        FROM FootballMatch m
        WHERE m.competitionCode = :competitionCode
          AND m.id <> :excludedMatchId
          AND m.status = 'FINISHED'
          AND m.homeScore IS NOT NULL
          AND m.awayScore IS NOT NULL
          AND m.kickoffAt < :referenceKickoffAt
          AND m.kickoffAt < :now
          AND (
              (
                  m.homeTeam.id = :homeTeamId
                  AND m.awayTeam.id = :awayTeamId
              )
              OR
              (
                  m.homeTeam.id = :awayTeamId
                  AND m.awayTeam.id = :homeTeamId
              )
          )
        ORDER BY m.kickoffAt DESC, m.id DESC
        """)
    List<FootballMatch> findHeadToHeadMatches(
            @Param("competitionCode") String competitionCode,
            @Param("excludedMatchId") Long excludedMatchId,
            @Param("homeTeamId") Long homeTeamId,
            @Param("awayTeamId") Long awayTeamId,
            @Param("referenceKickoffAt") Instant referenceKickoffAt,
            @Param("now") Instant now,
            Pageable pageable
    );
}