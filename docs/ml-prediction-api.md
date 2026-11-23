# ML Prediction API — v1

## Purpose

The Python service loads the saved v1 model at startup and returns
Premier League match outcome probabilities.

Spring Boot prepares features from PostgreSQL and calls this service.
React communicates with Spring Boot.

The prediction service does not query the database or external football APIs.

## Local startup

Run from ml-service with the virtual environment activated:

```powershell
python -m uvicorn app.main:app --reload --host 127.0.0.1 --port 8000
```

Base URL: http://127.0.0.1:8000
Interactive documentation: http://127.0.0.1:8000/docs

## Health and readiness

GET /health returns HTTP 200 when the application responds.

GET /ready returns HTTP 200 with:
- status
- model_version
- classes
- feature_count

The application fails startup if the model cannot be loaded or
its feature/class configuration is inconsistent.

## Predict

POST /predict
Content-Type: application/json

### Request

```json
{
  "home_points_last5": 15,
  "away_points_last5": 3,
  "home_goals_for_avg_last5": 2.8,
  "away_goals_for_avg_last5": 0.8,
  "home_goals_against_avg_last5": 0.6,
  "away_goals_against_avg_last5": 2.4
}
```

All six fields are required.
Additional fields are rejected.

Points must be integers between 0 and 15.
Goal averages must be finite, non-negative numbers.
Numeric strings and null values are rejected.

### Feature preparation rules

For each team:

1. Use completed Premier League matches with valid full-time scores.
2. Include both home and away appearances.
3. Use calendar dates in UTC consistently with the prepared dataset.
4. Include dates greater than or equal to match_date minus 365 days,
   and strictly earlier than match_date.
5. Sort chronologically and select the five most recent matches.
6. History may cross season boundaries.

Matches on the prediction date are excluded, even if they finished
earlier that day. The selected match and later matches are excluded.

If either team has fewer than five eligible matches, Spring Boot
must not call /predict. It should return an insufficient-history
result to the frontend without fabricated probabilities.

For each team's five selected matches:
- points_last5: total points, using 3 for a win, 1 for a draw, 0 for a loss.
- goals_for_avg_last5: total goals scored divided by 5.
- goals_against_avg_last5: total goals conceded divided by 5.

Goals must be interpreted from the selected team's perspective.
Do not round averages before sending them.

Use stable database team IDs to identify teams.
Only the six calculated features are sent to Python.

### Successful response

HTTP 200:

```json
{
  "status": "ok",
  "model_version": "v1",
  "predicted_outcome": "1",
  "probabilities": {
    "1": 0.777974365731627,
    "X": 0.1357233363198949,
    "2": 0.0863022979484781
  }
}
```

Outcome labels:
- 1: home win
- X: draw
- 2: away win

Probabilities are numbers between 0 and 1 and sum to approximately 1.
The predicted outcome has the highest probability.

Read probabilities by their JSON keys, not their position.
The model's internal class order is ["1", "2", "X"].

### Errors

- 422: missing, extra or invalid request fields.
- 503: model is not loaded.
- 500: internal prediction/configuration error.

If Python is unavailable or times out, Spring Boot must expose
prediction unavailability without preventing match details from loading.
Timeout limits will be configured during backend integration.

## Model and evaluation

- Version: v1
- Pipeline: StandardScaler + LogisticRegression
- C: 100
- Training seasons: 2019-20 through 2022-23
- Validation season: 2023-24
- Test season: 2024-25

Test results on 366 eligible matches:
- Accuracy: 0.480874
- Log loss: 1.033539

Frequency baseline on the same matches:
- Accuracy: 0.412568
- Log loss: 1.075387

## Limitations

- No draws were selected as the most likely outcome in validation or test.
- Five recent Premier League matches are required for each team.
- Fourteen test-season matches were excluded due to insufficient history.
- Probabilities are model estimates, not guaranteed outcomes.
- The model was trained on older seasons; performance on current seasons
  has not yet been measured.
- The HTTP endpoint validates feature values, but cannot verify that
  the supplied match history was complete or correctly selected.