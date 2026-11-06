# Match details API

## Endpoint

GET /api/matches/{id}

`id` is the internal database ID returned by GET /api/matches.
Data is read from PostgreSQL without calling football-data.org.

## Success — HTTP 200

Returns one JSON object, without a list or wrapper.

Verified response from GET /api/matches/2:

```json
{
  "id": 2,
  "homeTeam": {
    "id": 2,
    "name": "Arsenal FC",
    "shortName": "Arsenal",
    "crestUrl": "https://crests.football-data.org/57.png"
  },
  "awayTeam": {
    "id": 21,
    "name": "Coventry City FC",
    "shortName": "Coventry City",
    "crestUrl": "https://crests.football-data.org/1076.png"
  },
  "kickoffAt": "2026-08-21T19:00:00Z",
  "status": "FINISHED",
  "matchday": 1,
  "homeScore": 3,
  "awayScore": 0
}
```

## Errors

- HTTP 404: a positive ID that does not exist.
- HTTP 400: zero, negative, nonnumeric or out-of-range ID.

Verified HTTP 404 response:

```json
{
  "timestamp": "2026-11-06T16:01:50.957Z",
  "status": 404,
  "error": "Not Found",
  "path": "/api/matches/9223372036854775807"
}
```

Verified HTTP 400 response:

```json
{
  "timestamp": "2026-11-06T16:01:55.768Z",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/matches/0"
}
```

`timestamp` and `path` vary by request.
The current error response does not include a `message` field.
Frontend should use the HTTP status to choose its display message.

## Display rules

- Display kickoffAt in Europe/Skopje.
- Preserve null scores; do not replace them with zero.
- matchday, shortName and crestUrl may be null.