package com.beforekickoff.backend.repository;

import com.beforekickoff.backend.model.FootballMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FootballMatchRepository
        extends JpaRepository<FootballMatch, Long> {

    Optional<FootballMatch> findByExternalApiId(Long externalApiId);
}