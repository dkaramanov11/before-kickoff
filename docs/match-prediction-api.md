# Match Prediction API

## Endpoint

GET /api/matches/{id}/prediction

`id` is the internal database match ID returned by `/api/matches`.

Spring Boot reads match history from PostgreSQL, calculates the
features and calls the Python ML service. No football-data.org
request or automatic import is performed.

## Successful prediction

HTTP 200.

Actual local response for match 381:

```json
{
  "matchId": 381,
  "status": "ok",
  "modelVersion": "v1",
  "predictedOutcome": "2",
  "probabilities": {
    "homeWin": 0.31386496449077406,
    "draw": 0.25805840051686774,
    "awayWin": 0.42807663499235826
  }
}
```

Outcome labels:
- `1`: home win.
- `X`: draw.
- `2`: away win.

Probabilities are numbers between 0 and 1, not percentages.
They are model estimates, not guaranteed outcomes.

## Insufficient history

HTTP 200.

Actual local response for match 2:

```json
{
  "matchId": 2,
  "status": "insufficient_history",
  "modelVersion": null,
  "predictedOutcome": null,
  "probabilities": null
}
```

Returned when either team has fewer than five eligible matches.
The Python service is not called and probabilities are not fabricated.

## Prediction unavailable

HTTP 503.

Actual local response when the configured ML service was unreachable:

```json
{
  "matchId": 381,
  "status": "prediction_unavailable",
  "modelVersion": null,
  "predictedOutcome": null,
  "probabilities": null
}
```

Used when the ML request fails, times out, or returns an invalid
prediction response.

Match details remain available through GET /api/matches/{id}.

## History selection

For each team:
- Use only Premier League matches (`competitionCode = PL`).
- Require `FINISHED` status and non-negative, non-null final scores.
- Include both home and away appearances.
- Use the selected match's UTC calendar date.
- Include dates from match date minus 365 days, inclusive.
- Exclude the selected match's entire UTC date and all later dates.
- Exclude matches whose kickoff is not earlier than the request time.
- Order by kickoffAt descending, then id descending.
- Limit the database query to five matches.
- Allow history across seasons.
- Identify teams by internal database ID.

## Features

For each team's five selected matches:
- Points: 3 per win, 1 per draw, 0 per loss.
- Average goals scored: total goals scored / 5.
- Average goals conceded: total goals conceded / 5.

Goals are interpreted from that team's perspective.
Averages are sent without rounding.

Only the six features defined in `ml-prediction-api.md`
are sent to Python.

## Nullable fields

`matchId` and `status` are always present and non-null in prediction
responses.

`modelVersion`, `predictedOutcome` and `probabilities` are null for
`insufficient_history` and `prediction_unavailable`.

For `ok`, these fields and all three probability values are non-null.

## Invalid requests

- HTTP 400: invalid, non-positive or out-of-range ID.
- HTTP 404: no match exists for the supplied positive ID.
- HTTP 400: the selected match is not identified as Premier League.

Validation and not-found errors use the existing Spring Boot
error format, with `timestamp`, `status`, `error` and `path`.

Frontend should handle HTTP status codes and prediction `status`;
it must not depend on an error `message` being present.

## Configuration

Defaults:
- ML_BASE_URL: http://127.0.0.1:8000
- ML_CONNECT_TIMEOUT_MS: 2000
- ML_READ_TIMEOUT_MS: 5000

React communicates only with Spring Boot.

## Manual verification

Verified locally:
- Match 381: HTTP 200 with a v1 prediction.
- Match 2: HTTP 200 with insufficient_history.
- Unreachable ML service: HTTP 503 with prediction_unavailable.
- Match details still return HTTP 200 when ML is unreachable.
- Insufficient history still returns HTTP 200 when ML is unreachable.

Not yet verified for this endpoint:
- Delayed ML response exceeding the read timeout.
- HTTP 400 and HTTP 404 cases.
- Controlled history boundary and exclusion scenarios.

## Model limitations

Model v1 test accuracy was approximately 48.09% on 366 matches.
Test log loss was 1.033539.

The model did not select a draw as the most likely outcome
in validation or test.

Test accuracy is not confidence in an individual prediction.
Performance on current seasons has not yet been measured.   