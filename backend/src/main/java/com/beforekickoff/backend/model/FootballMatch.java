package com.beforekickoff.backend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "matches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FootballMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_api_id", unique = true)
    private Long externalApiId;

    @Column(name = "competition_code", length = 10)
    private String competitionCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Team awayTeam;

    @Column(name = "kickoff_at", nullable = false)
    private Instant kickoffAt;

    @Column(nullable = false, length = 30)
    private String status;

    @Column
    private Integer matchday;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    public FootballMatch(
            Long externalApiId,
            Team homeTeam,
            Team awayTeam,
            Instant kickoffAt,
            String status,
            Integer matchday,
            Integer homeScore,
            Integer awayScore
    ) {
        this.externalApiId = externalApiId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.kickoffAt = kickoffAt;
        this.status = status;
        this.matchday = matchday;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public void updateDetails(
            Team homeTeam,
            Team awayTeam,
            Instant kickoffAt,
            String status,
            Integer matchday,
            Integer homeScore,
            Integer awayScore
    ) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.kickoffAt = kickoffAt;
        this.status = status;
        this.matchday = matchday;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public void assignCompetition(String competitionCode) {
        this.competitionCode = competitionCode;
    }
}