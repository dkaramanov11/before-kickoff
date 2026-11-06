package com.beforekickoff.backend.repository;

import com.beforekickoff.backend.model.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;

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
}