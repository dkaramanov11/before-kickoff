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

## Initial inspection: Premier League 2023/24

- Source file: https://raw.githubusercontent.com/openfootball/football.json/master/2023-24/en.1.json
- Downloaded: 2026-11-18
- Local file: ml-service/data/raw/premier-league-2023-24.json
- All currently implemented checks passed.
- 380 matches, 20 teams, and 19 home plus 19 away matches per team.
- No duplicate ordered pairs, same-team matches, invalid dates, or invalid scores.

## Initial inspection: Premier League 2022/23

- Source file: https://raw.githubusercontent.com/openfootball/football.json/master/2022-23/en.1.json
- Local file: ml-service/data/raw/premier-league-2022-23.json
- All currently implemented checks passed.
- 380 matches, 20 teams, and 19 home plus 19 away matches per team.
- No duplicate ordered pairs, same-team matches, invalid dates, or invalid scores.

## Download a season

From ml-service:

    python scripts/download_data.py --season 2022-23

Existing files are not overwritten.
After downloading, run inspect_data.py for the same season.

## Additional inspected seasons

Source URL pattern:
https://raw.githubusercontent.com/openfootball/football.json/master/{season}/en.1.json

Local file pattern:
ml-service/data/raw/premier-league-{season}.json

| Season | Matches | Teams | All implemented checks passed |
|--------|---------|-------|-------------------------------|
| 2019-20 | 380 | 20 | Yes |
| 2020-21 | 380 | 20 | Yes |
| 2021-22 | 380 | 20 | Yes |

Each team has 19 home and 19 away matches.
No duplicate ordered pairs, same-team matches, invalid dates,
or invalid full-time scores were found.

The dataset now contains six seasons, from 2019/20 through 2024/25,
with 2,280 matches in total.

Team names have not yet been standardized across seasons.

## Team names and combined dataset

- Explicit aliases standardize 39 source names into 27 team names.
- Mapping: ml-service/data/mappings/team_aliases.json
- Preparation: python scripts/prepare_matches.py
- Output: ml-service/data/processed/matches.csv
- The combined dataset contains 2,280 matches from six seasons.
- Rows are sorted by date. Ordering within a date does not establish kickoff order.

## Known source date error: 2019/20

The original JSON contains 66 July fixtures dated 2019 instead of 2020.
Their dates were compared with:
https://www.11v11.com/competitions/premier-league/2020/matches/

prepare_matches.py changes the year only for 2019-07 dates in season 2019-20.
It requires exactly 66 affected records and stops otherwise.
Original JSON files remain unchanged.

After correction, preparation checks that all dates fall within
the broad August-to-August season windows.
These checks do not independently establish the accuracy of every match date.

## Run inspection

From ml-service with the virtual environment activated:

    python scripts/inspect_data.py

The script exits with code 1 if any implemented check fails.