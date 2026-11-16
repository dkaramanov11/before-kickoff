# Match history API

## Endpoint

GET /api/matches/{id}/history

The ID is the internal database match ID.

Reads only from PostgreSQL. Does not call football-data.org
or trigger an import.

## Response

HTTP 200 returns:
- matchId
- referenceKickoffAt
- homeTeam: teamId and recentMatches
- awayTeam: teamId and recentMatches
- headToHead

All lists contain FootballMatchResponse objects.
Lists are never null and contain at most five matches.

homeTeam and awayTeam refer to the teams in the selected match.
Their recent matches include both home and away appearances.

## Selection rules

- Only competitionCode = PL.
- Only FINISHED matches with both scores non-null.
- Exclude the selected match.
- kickoffAt must be strictly before the selected match's kickoffAt
  and strictly before the request time.
- Order by kickoffAt DESC, then id DESC.
- Limit each list to five records in the database query.
- Head-to-head includes both home/away combinations.
- Previous seasons are included when qualifying matches exist locally.

History reflects currently stored data. It does not reconstruct what
was known at the historical kickoff time.

## Actual empty-history response

Captured locally for match 2:

```json
{
  "matchId": 2,
  "referenceKickoffAt": "2026-08-21T19:00:00Z",
  "homeTeam": {
    "teamId": 2,
    "recentMatches": []
  },
  "awayTeam": {
    "teamId": 21,
    "recentMatches": []
  },
  "headToHead": []
}
```

## Form

Frontend derives W/D/L from recentMatches from the relevant team's
perspective. Matches arrive newest first; form badges are displayed
oldest first.

No separate form field is returned.

## Nullable fields

Inside each FootballMatchResponse:
- matchday
- homeTeam.shortName and awayTeam.shortName
- homeTeam.crestUrl and awayTeam.crestUrl

Scores in history are non-null because incomplete scores are excluded.

## Errors

- 400: non-positive, non-numeric, or out-of-range Long ID.
- 404: positive ID that does not exist.
- 200 with empty lists: existing match without qualifying history.

Actual error response captured locally:

```json
{
  "timestamp": "2026-11-16T15:07:52.240Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/matches/0/history"
}
```

Frontend handles errors by HTTP status and does not depend on message.

## League standings

Use the existing GET /api/standings/premier-league endpoint.

Highlight teams using their internal IDs. Standings show the latest
saved snapshot, not the table at the selected match's kickoff.

## Manual verification

Passed:
- Match 375 returned five recent matches per team and one head-to-head.
- Recent matches included home and away appearances.
- Head-to-head included reversed home/away roles.
- Returned matches were ordered newest first.
- Empty history for match 2.
- Finished-match response contained no selected match, invalid status,
  missing scores, or kickoff at/after either time boundary.
- HTTP 400 for 0, -1 and abc.
- HTTP 404 for 9223372036854775807.

Not yet tested with controlled data:
- Equal kickoff timestamps with id DESC tie-breaking.
- FINISHED matches with null scores.
- Matches exactly on the time boundaries.
- Exclusion of another competition.