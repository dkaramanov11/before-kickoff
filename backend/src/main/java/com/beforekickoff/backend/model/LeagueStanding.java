package com.beforekickoff.backend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "league_standings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeagueStanding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "competition_code", nullable = false,
            unique = true, length = 10)
    private String competitionCode;

    @Column(name = "competition_name", nullable = false, length = 150)
    private String competitionName;

    @Column(name = "season_start_year", nullable = false)
    private Integer seasonStartYear;

    @Column(name = "last_synced_at", nullable = false)
    private Instant lastSyncedAt;

    public LeagueStanding(
            String competitionCode,
            String competitionName,
            Integer seasonStartYear,
            Instant lastSyncedAt
    ) {
        this.competitionCode = competitionCode;
        updateDetails(competitionName, seasonStartYear, lastSyncedAt);
    }

    public void updateDetails(
            String competitionName,
            Integer seasonStartYear,
            Instant lastSyncedAt
    ) {
        this.competitionName = competitionName;
        this.seasonStartYear = seasonStartYear;
        this.lastSyncedAt = lastSyncedAt;
    }
}