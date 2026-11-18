import json
from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent
DATA_FILE = BASE_DIR / "data" / "raw" / "premier-league-2024-25.json"

with DATA_FILE.open(encoding="utf-8") as file:
    data = json.load(file)

matches = pd.json_normalize(data["matches"])

print("Competition:", data["name"])
print("Number of matches:", len(matches))
print()
print(matches[["date", "team1", "team2", "score.ft"]].head(5).to_string(index=False))

home_counts = matches["team1"].value_counts()
away_counts = matches["team2"].value_counts()

team_counts = pd.DataFrame({
    "home": home_counts,
    "away": away_counts,
}).fillna(0).astype(int)

team_counts["total"] = team_counts["home"] + team_counts["away"]
team_counts = team_counts.sort_index()

print()
print("Number of teams:", len(team_counts))
print()
print(team_counts.to_string())

valid_counts = (
    (len(matches) == 380)
    and (len(team_counts) == 20)
    and (team_counts["home"] == 19).all()
    and (team_counts["away"] == 19).all()
)

print()
print("Match counts valid:", valid_counts)

duplicate_pairs = matches.duplicated(
    subset=["team1", "team2"]
).sum()

same_team_matches = (matches["team1"] == matches["team2"]).sum()

parsed_dates = pd.to_datetime(
    matches["date"],
    format="%Y-%m-%d",
    errors="coerce",
)

invalid_dates = parsed_dates.isna().sum()


def is_valid_score(score):
    return (
            isinstance(score, list)
            and len(score) == 2
            and all(type(goal) is int and goal >= 0 for goal in score)
    )


invalid_scores = (~matches["score.ft"].apply(is_valid_score)).sum()

print()
print("Duplicate home-away pairs:", duplicate_pairs)
print("Matches with the same team:", same_team_matches)
print("Invalid or missing dates:", invalid_dates)
print("Invalid or missing scores:", invalid_scores)

all_checks_passed = (
        valid_counts
        and duplicate_pairs == 0
        and same_team_matches == 0
        and invalid_dates == 0
        and invalid_scores == 0
)

print()
print("All checks passed:", all_checks_passed)

if not all_checks_passed:
    raise SystemExit(1)