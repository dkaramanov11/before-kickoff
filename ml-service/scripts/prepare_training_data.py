from pathlib import Path

import pandas as pd

from inspect_match_features import build_match_features, get_match_target


BASE_DIR = Path(__file__).resolve().parent.parent
INPUT_FILE = BASE_DIR / "data" / "processed" / "matches.csv"
OUTPUT_FILE = BASE_DIR / "data" / "processed" / "training_data.csv"


if __name__ == "__main__":
    matches = pd.read_csv(INPUT_FILE, parse_dates=["date"])
    matches = matches.sort_values(
        ["date", "home_team", "away_team"]
    ).reset_index(drop=True)

    rows = []
    skipped = 0

    for match in matches.itertuples(index=False):
        features = build_match_features(
            matches,
            match.home_team,
            match.away_team,
            match.date,
        )

        if features is None:
            skipped += 1
            continue

        rows.append({
            "season": match.season,
            "date": match.date,
            "home_team": match.home_team,
            "away_team": match.away_team,
            **features,
            "target": get_match_target(
                match.home_goals,
                match.away_goals,
            ),
        })

    training_data = pd.DataFrame(rows)

    if training_data.empty:
        raise ValueError("No matches have sufficient history.")

    if training_data.isna().any().any():
        raise ValueError("Training data contains missing values.")

    if not training_data["target"].isin(["1", "X", "2"]).all():
        raise ValueError("Training data contains invalid targets.")

    if len(training_data) + skipped != len(matches):
        raise ValueError("Prepared and skipped counts do not match input.")

    OUTPUT_FILE.parent.mkdir(parents=True, exist_ok=True)
    training_data.to_csv(
        OUTPUT_FILE,
        index=False,
        date_format="%Y-%m-%d",
    )

    print("Input matches:", len(matches))
    print("Prepared rows:", len(training_data))
    print("Skipped due to insufficient history:", skipped)
    print()
    print("Prepared rows by season:")
    print(training_data.groupby("season").size().to_string())
    print()
    print(training_data.head(3).to_string(index=False))