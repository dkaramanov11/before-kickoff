from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent
DATA_FILE = BASE_DIR / "data" / "processed" / "matches.csv"


def get_previous_matches(matches, team_name, match_date):
    match_date = pd.Timestamp(match_date)
    earliest_date = match_date - pd.Timedelta(days=365)

    is_team = (
            (matches["home_team"] == team_name)
            | (matches["away_team"] == team_name)
    )

    is_previous = (
            (matches["date"] >= earliest_date)
            & (matches["date"] < match_date)
    )

    return (
        matches.loc[is_team & is_previous]
        .sort_values("date")
        .tail(5)
        .copy()
    )

def calculate_team_form(previous_matches, team_name):
    if len(previous_matches) != 5:
        return None

    points = 0
    goals_for = 0
    goals_against = 0

    for match in previous_matches.itertuples(index=False):
        if match.home_team == team_name:
            scored = match.home_goals
            conceded = match.away_goals
        else:
            scored = match.away_goals
            conceded = match.home_goals

        goals_for += scored
        goals_against += conceded

        if scored > conceded:
            points += 3
        elif scored == conceded:
            points += 1

    return {
        "points_last5": points,
        "goals_for_avg_last5": goals_for / 5,
        "goals_against_avg_last5": goals_against / 5,
    }

if __name__ == "__main__":
    matches = pd.read_csv(DATA_FILE, parse_dates=["date"])

    previous = get_previous_matches(
        matches,
        team_name="Arsenal FC",
        match_date="2024-08-17",
    )

    print(previous.to_string(index=False))
    print()
    print("Previous matches found:", len(previous))
    print("Enough history:", len(previous) == 5)
    form = calculate_team_form(previous, "Arsenal FC")
    print("Team form:", form)