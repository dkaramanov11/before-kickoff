# ML historical data

## Source

- Provider: OpenFootball
- Repository: https://github.com/openfootball/football.json
- Repository license: CC0-1.0
- Source file: https://raw.githubusercontent.com/openfootball/football.json/master/2024-25/en.1.json
- Downloaded: 2026-11-18
- Local file: ml-service/data/raw/premier-league-2024-25.json

The downloaded JSON is stored unchanged.
The source URL uses the master branch and may change over time.

## Initial inspection: Premier League 2024/25

Passed checks:
- 380 matches and 20 teams.
- Each team has 19 home and 19 away matches.
- No duplicate ordered home-away pairs.
- No matches with the same home and away team.
- No invalid or missing dates.
- All full-time scores contain two non-negative integers.

These checks establish structural consistency.
They do not independently verify every result or kickoff time.

## Run inspection

From ml-service with the virtual environment activated:

    python scripts/inspect_data.py

The script exits with code 1 if any implemented check fails.