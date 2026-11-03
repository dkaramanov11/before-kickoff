package com.beforekickoff.backend.repository;

import com.beforekickoff.backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByExternalApiId(Long externalApiId);
}