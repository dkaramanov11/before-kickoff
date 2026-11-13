package com.beforekickoff.backend.repository;

import com.beforekickoff.backend.model.LeagueStandingEntry;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeagueStandingEntryRepository
        extends JpaRepository<LeagueStandingEntry, Long> {

    @EntityGraph(attributePaths = "team")
    List<LeagueStandingEntry>
    findAllByLeagueStanding_IdOrderByPositionAsc(Long leagueStandingId);

    @Modifying
    @Query("""
            DELETE FROM LeagueStandingEntry entry
            WHERE entry.leagueStanding.id = :standingId
            """)
    int deleteEntriesByStandingId(@Param("standingId") Long standingId);
}