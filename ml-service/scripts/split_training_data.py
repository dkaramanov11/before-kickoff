from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent
DATA_DIR = BASE_DIR / "data" / "processed"

TRAIN_SEASONS = ["2019-20", "2020-21", "2021-22", "2022-23"]
VALIDATION_SEASONS = ["2023-24"]
TEST_SEASONS = ["2024-25"]


def split_training_data(data):
    expected_seasons = set(
        TRAIN_SEASONS + VALIDATION_SEASONS + TEST_SEASONS
    )

    if data.empty:
        raise ValueError("Training data is empty.")

    if data.isna().any().any():
        raise ValueError("Training data contains missing values.")

    if set(data["season"]) != expected_seasons:
        raise ValueError("Unexpected or missing seasons.")

    if data.duplicated(["season", "home_team", "away_team"]).any():
        raise ValueError("Duplicate matches found.")

    data = data.sort_values(
        ["date", "home_team", "away_team"]
    ).reset_index(drop=True)

    train = data.loc[data["season"].isin(TRAIN_SEASONS)].copy()
    validation = data.loc[
        data["season"].isin(VALIDATION_SEASONS)
    ].copy()
    test = data.loc[data["season"].isin(TEST_SEASONS)].copy()

    if train.empty or validation.empty or test.empty:
        raise ValueError("One or more splits are empty.")

    if not (
            train["date"].max() < validation["date"].min()
            and validation["date"].max() < test["date"].min()
    ):
        raise ValueError("Split dates overlap or are out of order.")

    if len(train) + len(validation) + len(test) != len(data):
        raise ValueError("Some rows were lost during splitting.")

    return train, validation, test


if __name__ == "__main__":
    data = pd.read_csv(
        DATA_DIR / "training_data.csv",
        parse_dates=["date"],
        dtype={"target": str},
        )

    train, validation, test = split_training_data(data)

    for name, split in [
        ("train", train),
        ("validation", validation),
        ("test", test),
    ]:
        output_file = DATA_DIR / f"{name}.csv"
        split.to_csv(output_file, index=False, date_format="%Y-%m-%d")

        first_date = split["date"].min().date()
        last_date = split["date"].max().date()

        print(f"{name}: {len(split)} rows | {first_date} -> {last_date}")

    print("Chronological split checks passed.")