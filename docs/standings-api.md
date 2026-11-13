# Premier League standings API

## Read saved standings

GET /api/standings/premier-league

Returns HTTP 200 with the latest successfully imported TOTAL standings
from PostgreSQL. This endpoint does not call football-data.org.

No query parameters are required. The standings are independent of
the date selected in the matches list.

### Successful response

Actual response captured on 2026-11-13, shortened to the first entry.
The full response contains 20 entries.

```json
{
  "competitionCode": "PL",
  "competitionName": "Premier League",
  "seasonStartYear": 2026,
  "lastSyncedAt": "2026-11-13T15:10:30.917447Z",
  "standings": [
    {
      "position": 1,
      "team": {
        "id": 2,
        "name": "Arsenal FC",
        "shortName": "Arsenal",
        "crestUrl": "https://crests.football-data.org/57.png"
      },
      "playedGames": 10,
      "won": 9,
      "draw": 0,
      "lost": 1,
      "goalsFor": 15,
      "goalsAgainst": 5,
      "goalDifference": 10,
      "points": 27
    }
  ]
}
```

### Field rules

- Entries are ordered by the official position, ascending.
- Position and points are copied from the source, including points deductions.
- Points and goalDifference can be negative.
- team.id is the internal database ID, not the football-data.org ID.
- team.shortName and team.crestUrl can be null.
- All other fields in a successful response are non-null.
- goalsFor and goalsAgainst are separate numbers.
- lastSyncedAt is an ISO 8601 UTC timestamp marking our successful import.
  It is not the source provider's update time.
- seasonStartYear is the year in which the season starts.
- The backend stores one latest snapshot per competition, without snapshot history.

### No imported standings

Returns HTTP 404 when no Premier League standings have been imported.

The service does not fetch external data automatically.

### Errors

The current application uses the default Spring Boot JSON error structure.
Example structure for HTTP 404 (timestamp is illustrative):

```json
{
  "timestamp": "2026-11-13T16:00:00.000Z",
  "status": 404,
  "error": "Not Found",
  "path": "/api/standings/premier-league"
}
```

A message field is not guaranteed. Frontend should handle errors using
the HTTP status, rather than depending on a message.

Unexpected server failures return HTTP 500.

## Manual import

POST /api/standings/import/premier-league

No request body is required.

Fetches the current standings from football-data.org without date or
matchday filters and selects the TOTAL table.

Teams must already exist in the local database. External team IDs are
resolved to internal teams using externalApiId.

### Successful response

HTTP 200:

```json
{
  "processedTeams": 20
}
```

### Refresh behavior

- Imports are currently manual and should run one at a time.
- All entries are validated before replacing the saved standings.
- Repeated imports replace the previous entries without accumulating duplicates.
- Metadata and entries are saved in one transaction.
- A failed import preserves the previous snapshot and lastSyncedAt.
- Import failures currently return HTTP 500 through the default error handling.

## Verified manually

- Import returned processedTeams = 20.
- A repeated import left 20 entries and 20 distinct internal team IDs.
- lastSyncedAt changed after a successful repeated import.
- Repeated GET requests left lastSyncedAt unchanged.

The never-imported HTTP 404 behavior is defined in the service code,
but has not yet been manually verified for this feature.