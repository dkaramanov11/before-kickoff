package com.beforekickoff.backend.service.impl;

import com.beforekickoff.backend.dto.team.TeamMatchesResponse;
import com.beforekickoff.backend.repository.FootballMatchRepository;
import com.beforekickoff.backend.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TeamServiceImplTest {

    private TeamRepository teamRepository;
    private FootballMatchRepository matchRepository;
    private TeamServiceImpl service;

    @BeforeEach
    void setUp() {
        teamRepository = mock(TeamRepository.class);
        matchRepository = mock(FootballMatchRepository.class);

        service = new TeamServiceImpl(
                teamRepository,
                matchRepository
        );
    }

    @Test
    void rejectsInvalidIdsWithoutQueryingRepositories() {
        Long[] invalidIds = {null, 0L, -1L};

        for (Long id : invalidIds) {
            ResponseStatusException exception = assertThrows(
                    ResponseStatusException.class,
                    () -> service.getTeamMatches(id)
            );

            assertEquals(
                    HttpStatus.BAD_REQUEST,
                    exception.getStatusCode()
            );
        }

        verifyNoInteractions(teamRepository, matchRepository);
    }

    @Test
    void returnsNotFoundForMissingTeam() {
        when(teamRepository.existsById(999L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getTeamMatches(999L)
        );

        assertEquals(
                HttpStatus.NOT_FOUND,
                exception.getStatusCode()
        );

        verifyNoInteractions(matchRepository);
    }

    @Test
    void returnsEmptyListsForExistingTeamWithoutMatches() {
        stubEmptyHistory(2L);

        TeamMatchesResponse response = service.getTeamMatches(2L);

        assertEquals(Long.valueOf(2L), response.teamId());
        assertNotNull(response.recentMatches());
        assertNotNull(response.upcomingMatches());
        assertTrue(response.recentMatches().isEmpty());
        assertTrue(response.upcomingMatches().isEmpty());
    }

    @Test
    void usesSameTimeAndFiveRecordLimitForBothQueries() {
        stubEmptyHistory(2L);

        Instant before = Instant.now();

        service.getTeamMatches(2L);

        Instant after = Instant.now();

        ArgumentCaptor<Instant> recentTime =
                ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<Instant> upcomingTime =
                ArgumentCaptor.forClass(Instant.class);

        ArgumentCaptor<Pageable> recentPage =
                ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<Pageable> upcomingPage =
                ArgumentCaptor.forClass(Pageable.class);

        verify(matchRepository).findTeamRecentMatches(
                eq(2L),
                recentTime.capture(),
                recentPage.capture()
        );

        verify(matchRepository).findTeamUpcomingMatches(
                eq(2L),
                upcomingTime.capture(),
                upcomingPage.capture()
        );

        assertEquals(
                recentTime.getValue(),
                upcomingTime.getValue()
        );

        assertFalse(recentTime.getValue().isBefore(before));
        assertFalse(recentTime.getValue().isAfter(after));

        assertEquals(0, recentPage.getValue().getPageNumber());
        assertEquals(5, recentPage.getValue().getPageSize());

        assertEquals(0, upcomingPage.getValue().getPageNumber());
        assertEquals(5, upcomingPage.getValue().getPageSize());
    }

    private void stubEmptyHistory(Long teamId) {
        when(teamRepository.existsById(teamId)).thenReturn(true);

        when(matchRepository.findTeamRecentMatches(
                eq(teamId),
                any(Instant.class),
                any(Pageable.class)
        )).thenReturn(List.of());

        when(matchRepository.findTeamUpcomingMatches(
                eq(teamId),
                any(Instant.class),
                any(Pageable.class)
        )).thenReturn(List.of());
    }
}