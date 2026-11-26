# Team Details API

## Overview

All GET endpoints read only from PostgreSQL.
No football-data.org or Python ML requests are made.
No automatic import is performed.

All IDs are internal database IDs.

## 1. Team details

GET /api/teams/{id}

HTTP 200 returns one JSON object, without a list or wrapper.

Actual local response for GET /api/teams/2:

```json
{
  "id": 2,
  "name": "Arsenal FC",
  "shortName": "Arsenal",
  "crestUrl": "https://crests.football-data.org/57.png"
}
```

- id and name are required and non-null.
- shortName and crestUrl may be null.
- A team may exist without matches or a current standings entry.

## 2. Recent and upcoming matches

GET /api/teams/{id}/matches

Both lists are always present and never null.
Each list contains at most five matches.
Each item uses the existing FootballMatchResponse format.

Actual local response for GET /api/teams/2/matches:

```json
{
  "teamId": 2,
  "recentMatches": [
    {
      "id": 94,
      "homeTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "awayTeam": {
        "id": 13,
        "name": "Hull City AFC",
        "shortName": "Hull City",
        "crestUrl": "https://crests.football-data.org/322.png"
      },
      "kickoffAt": "2026-11-07T15:00:00Z",
      "status": "FINISHED",
      "matchday": 10,
      "homeScore": 1,
      "awayScore": 0
    },
    {
      "id": 90,
      "homeTeam": {
        "id": 7,
        "name": "Liverpool FC",
        "shortName": "Liverpool",
        "crestUrl": "https://crests.football-data.org/64.png"
      },
      "awayTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "kickoffAt": "2026-11-01T16:30:00Z",
      "status": "FINISHED",
      "matchday": 9,
      "homeScore": 0,
      "awayScore": 1
    },
    {
      "id": 74,
      "homeTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "awayTeam": {
        "id": 5,
        "name": "Everton FC",
        "shortName": "Everton",
        "crestUrl": "https://crests.football-data.org/62.png"
      },
      "kickoffAt": "2026-10-24T14:00:00Z",
      "status": "FINISHED",
      "matchday": 8,
      "homeScore": 2,
      "awayScore": 0
    },
    {
      "id": 70,
      "homeTeam": {
        "id": 16,
        "name": "Nottingham Forest FC",
        "shortName": "Nottingham",
        "crestUrl": "https://crests.football-data.org/351.png"
      },
      "awayTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "kickoffAt": "2026-10-18T15:30:00Z",
      "status": "FINISHED",
      "matchday": 7,
      "homeScore": 0,
      "awayScore": 1
    },
    {
      "id": 52,
      "homeTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "awayTeam": {
        "id": 14,
        "name": "Leeds United FC",
        "shortName": "Leeds United",
        "crestUrl": "https://crests.football-data.org/341.png"
      },
      "kickoffAt": "2026-10-10T11:30:00Z",
      "status": "FINISHED",
      "matchday": 6,
      "homeScore": 2,
      "awayScore": 1
    }
  ],
  "upcomingMatches": [
    {
      "id": 121,
      "homeTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "awayTeam": {
        "id": 8,
        "name": "Manchester City FC",
        "shortName": "Man City",
        "crestUrl": "https://crests.football-data.org/65.png"
      },
      "kickoffAt": "2026-11-29T16:30:00Z",
      "status": "TIMED",
      "matchday": 12,
      "homeScore": null,
      "awayScore": null
    },
    {
      "id": 128,
      "homeTeam": {
        "id": 19,
        "name": "Brentford FC",
        "shortName": "Brentford",
        "crestUrl": "https://crests.football-data.org/402.png"
      },
      "awayTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "kickoffAt": "2026-12-02T20:15:00Z",
      "status": "TIMED",
      "matchday": 13,
      "homeScore": null,
      "awayScore": null
    },
    {
      "id": 141,
      "homeTeam": {
        "id": 12,
        "name": "Tottenham Hotspur FC",
        "shortName": "Tottenham",
        "crestUrl": "https://crests.football-data.org/73.png"
      },
      "awayTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "kickoffAt": "2026-12-06T16:30:00Z",
      "status": "TIMED",
      "matchday": 14,
      "homeScore": null,
      "awayScore": null
    },
    {
      "id": 148,
      "homeTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "awayTeam": {
        "id": 20,
        "name": "AFC Bournemouth",
        "shortName": "Bournemouth",
        "crestUrl": "https://crests.football-data.org/bournemouth.png"
      },
      "kickoffAt": "2026-12-13T14:00:00Z",
      "status": "TIMED",
      "matchday": 15,
      "homeScore": null,
      "awayScore": null
    },
    {
      "id": 160,
      "homeTeam": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "awayTeam": {
        "id": 9,
        "name": "Manchester United FC",
        "shortName": "Man United",
        "crestUrl": "https://crests.football-data.org/66.png"
      },
      "kickoffAt": "2026-12-20T16:30:00Z",
      "status": "TIMED",
      "matchday": 16,
      "homeScore": null,
      "awayScore": null
    }
  ]
}
```

### Recent matches

- Premier League only: competitionCode = PL.
- Includes home and away appearances.
- Status must be FINISHED.
- Both final scores must be non-null.
- kickoffAt must be strictly before the request cutoff.
- Order: kickoffAt DESC, then id DESC.
- Limited to five records in the database query.
- May include previous seasons.
- No 365-day restriction.

### Upcoming matches

- Premier League only: competitionCode = PL.
- Includes home and away appearances.
- Status must be SCHEDULED or TIMED.
- kickoffAt must be strictly after the request cutoff.
- Order: kickoffAt ASC, then id ASC.
- Limited to five records in the database query.
- Excludes past, postponed, cancelled, suspended and in-progress matches.

Both queries use the same Instant cutoff.
A match exactly at the cutoff is excluded from both lists.

### Dates and nullable fields

- kickoffAt is an ISO 8601 timestamp in UTC.
- Frontend displays dates using Europe/Skopje.
- matchday may be null.
- Team shortName and crestUrl may be null.
- Upcoming homeScore and awayScore may be null.
- Unavailable scores remain null, not zero.
- Recent matches always have both scores.

## 3. Empty state

An existing team without eligible matches returns HTTP 200.
Either list may be empty independently.

Illustrative example:

```json
{
  "teamId": 2,
  "recentMatches": [],
  "upcomingMatches": []
}
```

## 4. Team form

No separate endpoint or field is provided.

Frontend derives W / D / L from recentMatches,
from the selected team's perspective:
- W: win.
- D: draw.
- L: loss.

Backend returns matches newest first.
Frontend displays form oldest first using a copy of the array.

If fewer than five results exist, display only those available.

## 5. Current standings

Use the existing GET /api/standings/premier-league.

Find the entry where entry.team.id matches the selected team ID.

Use:
- position
- playedGames
- won
- draw
- lost
- goalsFor
- goalsAgainst
- goalDifference
- points

Points and goalDifference may be negative.
Use stored official values; do not calculate standings
from the five recent matches.

The table is the latest successfully imported state.
seasonStartYear and lastSyncedAt identify its season and freshness.

No imported table produces the existing standings HTTP 404.
A missing team entry means current standings are unavailable
for that team.

Standings unavailability must not prevent team details
or matches from loading.

## 6. Errors

Both new endpoints use:
- HTTP 400: non-positive, non-numeric or out-of-range Long ID.
- HTTP 404: no team exists for the supplied positive ID.
- HTTP 500: unexpected server error.

Errors use the existing Spring Boot JSON format:
timestamp, status, error and path.

Frontend handles HTTP status and does not depend on message.
An existing team without matches returns 200, not 404.

## 7. Frontend navigation

Team route: /teams/:id.

Match items link to the existing match details page
using the match's internal ID.

No navigation URLs are added to the API response.

## 8. Verification

Manually verified in the supplied local responses for team 2:
- Team details returned successfully.
- Five recent and five upcoming matches were returned.
- Home and away appearances were present.
- Recent matches were ordered newest first.
- Upcoming matches were ordered earliest first.
- Upcoming scores remained null.

Not confirmed by the captured responses:
- Null shortName and crestUrl.
- Existing team without eligible matches.
- Equal kickoff timestamps with ID tie ordering.
- Match exactly at the request cutoff.
- Exclusion of other competitions and all disallowed statuses.
- HTTP 400 and 404.
- Regression checks for existing matches and standings endpoints.

Automated tests for this feature were not completed and were removed.
The attempted Maven run failed before tests executed because
the terminal Java environment did not provide a compiler.