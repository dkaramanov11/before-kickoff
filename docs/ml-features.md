# ML features — version 1

## Input features

For each team, use its five most recent Premier League matches:
- Total points: win = 3, draw = 1, loss = 0.
- Average goals scored.
- Average goals conceded.

Feature columns:
- home_points_last5
- away_points_last5
- home_goals_for_avg_last5
- away_goals_for_avg_last5
- home_goals_against_avg_last5
- away_goals_against_avg_last5

Home and away appearances are combined when calculating team form.

## History rules

- Include dates from match_date minus 365 days, inclusive.
- Exclude the match date and all later dates.
- History can continue across season boundaries.
- Require five previous matches for both teams.
- Skip matches with insufficient history; do not substitute zeros.
- Prediction must use the same eligibility rules.

## Target

- 1: home win
- X: draw
- 2: away win

The current match result is used only for the target.

## Dataset

- Input matches: 2,280
- Eligible rows: 2,158
- Skipped for insufficient history: 122

| Season | Eligible rows |
|--------|---------------|
| 2019-20 | 330 |
| 2020-21 | 366 |
| 2021-22 | 366 |
| 2022-23 | 365 |
| 2023-24 | 365 |
| 2024-25 | 366 |

## Generate

From ml-service:

    python scripts/prepare_training_data.py

Output: data/processed/training_data.csv

Season, date, and team names are metadata.
Only the six listed feature columns will be model inputs.

## Limitations

Teams without sufficient recent Premier League history are excluded.
Evaluation therefore measures performance only on eligible matches.
Separate home/away form and opponent strength are not included.