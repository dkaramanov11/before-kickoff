package com.beforekickoff.backend.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "league_standing_entries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_standing_entries_team",
                        columnNames = {"league_standing_id", "team_id"}
                ),
                @UniqueConstraint(
                        name = "uq_standing_entries_position",
                        columnNames = {"league_standing_id", "position"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeagueStandingEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "league_standing_id", nullable = false)
    private LeagueStanding leagueStanding;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "played_games", nullable = false)
    private Integer playedGames;

    @Column(nullable = false)
    private Integer won;

    @Column(nullable = false)
    private Integer draw;

    @Column(nullable = false)
    private Integer lost;

    @Column(name = "goals_for", nullable = false)
    private Integer goalsFor;

    @Column(name = "goals_against", nullable = false)
    private Integer goalsAgainst;

    @Column(name = "goal_difference", nullable = false)
    private Integer goalDifference;

    @Column(nullable = false)
    private Integer points;

    public LeagueStandingEntry(
            LeagueStanding leagueStanding,
            Team team,
            Integer position,
            Integer playedGames,
            Integer won,
            Integer draw,
            Integer lost,
            Integer goalsFor,
            Integer goalsAgainst,
            Integer goalDifference,
            Integer points
    ) {
        this.leagueStanding = leagueStanding;
        this.team = team;
        this.position = position;
        this.playedGames = playedGames;
        this.won = won;
        this.draw = draw;
        this.lost = lost;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalDifference = goalDifference;
        this.points = points;
    }
}