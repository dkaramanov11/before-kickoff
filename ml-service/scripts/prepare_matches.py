import json
from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent
RAW_DIR = BASE_DIR / "data" / "raw"
MAPPING_FILE = BASE_DIR / "data" / "mappings" / "team_aliases.json"
OUTPUT_FILE = BASE_DIR / "data" / "processed" / "matches.csv"

SEASONS = [
    "2019-20",
    "2020-21",
    "2021-22",
    "2022-23",
    "2023-24",
    "2024-25",
]

with MAPPING_FILE.open(encoding="utf-8") as file:
    aliases = json.load(file)

rows = []

for season in SEASONS:
    file_path = RAW_DIR / f"premier-league-{season}.json"

    with file_path.open(encoding="utf-8") as file:
        data = json.load(file)

    for match in data["matches"]:
        home_goals, away_goals = match["score"]["ft"]

        rows.append({
            "season": season,
            "date": match["date"],
            "home_team": aliases.get(match["team1"], match["team1"]),
            "away_team": aliases.get(match["team2"], match["team2"]),
            "home_goals": home_goals,
            "away_goals": away_goals,
        })

matches = pd.DataFrame(rows)

# Known source error: July 2020 fixtures are dated July 2019.
july_date_error = (
        (matches["season"] == "2019-20")
        & matches["date"].str.startswith("2019-07-")
)

correction_count = int(july_date_error.sum())

if correction_count != 66:
    raise ValueError(
        f"Expected 66 known date corrections, found {correction_count}. "
        "Review the source before continuing."
    )

matches.loc[july_date_error, "date"] = (
        "2020" + matches.loc[july_date_error, "date"].str[4:]
)

print("Corrected source dates:", correction_count)

matches["date"] = pd.to_datetime(
    matches["date"],
    format="%Y-%m-%d",
    errors="raise",
)

for season in SEASONS:
    start_year = int(season[:4])
    start = pd.Timestamp(year=start_year, month=8, day=1)
    end = pd.Timestamp(year=start_year + 1, month=8, day=1)

    season_dates = matches.loc[matches["season"] == season, "date"]
    outside = (season_dates < start) | (season_dates >= end)

    if outside.any():
        raise ValueError(f"Dates outside season window: {season}")

print("Processed season date windows: OK")

matches = matches.sort_values(
    ["date", "home_team", "away_team"]
).reset_index(drop=True)

OUTPUT_FILE.parent.mkdir(parents=True, exist_ok=True)
matches.to_csv(OUTPUT_FILE, index=False, date_format="%Y-%m-%d")

print("Prepared matches:", len(matches))
print("Seasons:", matches["season"].nunique())
print("Saved to:", OUTPUT_FILE)
print()
print(matches.head(5).to_string(index=False))