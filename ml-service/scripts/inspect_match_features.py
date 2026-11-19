from pathlib import Path

import pandas as pd

from inspect_team_form import get_previous_matches, calculate_team_form


BASE_DIR = Path(__file__).resolve().parent.parent
DATA_FILE = BASE_DIR / "data" / "processed" / "matches.csv"


def build_match_features(matches, home_team, away_team, match_date):
    if home_team == away_team:
        raise ValueError("Home and away teams must be different.")

    home_previous = get_previous_matches(matches, home_team, match_date)
    away_previous = get_previous_matches(matches, away_team, match_date)

    home_form = calculate_team_form(home_previous, home_team)
    away_form = calculate_team_form(away_previous, away_team)

    if home_form is None or away_form is None:
        return None

    return {
        "home_points_last5": home_form["points_last5"],
        "away_points_last5": away_form["points_last5"],
        "home_goals_for_avg_last5": home_form["goals_for_avg_last5"],
        "away_goals_for_avg_last5": away_form["goals_for_avg_last5"],
        "home_goals_against_avg_last5": home_form["goals_against_avg_last5"],
        "away_goals_against_avg_last5": away_form["goals_against_avg_last5"],
    }

def get_match_target(home_goals, away_goals):
    if home_goals > away_goals:
        return "1"
    if home_goals == away_goals:
        return "X"
    return "2"

if __name__ == "__main__":
    matches = pd.read_csv(DATA_FILE, parse_dates=["date"])

    features = build_match_features(
        matches,
        home_team="Arsenal FC",
        away_team="Wolverhampton Wanderers FC",
        match_date="2024-08-17",
    )

    if features is None:
        print("Not enough history for one or both teams.")
    else:
        print(pd.DataFrame([features]).to_string(index=False))

    assert get_match_target(2, 0) == "1"
    assert get_match_target(1, 1) == "X"
    assert get_match_target(0, 3) == "2"

    print("Target mapping checks passed.")