import json
from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent
RAW_DIR = BASE_DIR / "data" / "raw"

SEASON_WINDOWS = {
    "2019-20": ("2019-08-01", "2020-08-01"),
    "2020-21": ("2020-08-01", "2021-08-01"),
    "2021-22": ("2021-08-01", "2022-08-01"),
    "2022-23": ("2022-08-01", "2023-08-01"),
    "2023-24": ("2023-08-01", "2024-08-01"),
    "2024-25": ("2024-08-01", "2025-08-01"),
}

for season, (start, end) in SEASON_WINDOWS.items():
    file_path = RAW_DIR / f"premier-league-{season}.json"

    with file_path.open(encoding="utf-8") as file:
        data = json.load(file)

    matches = pd.json_normalize(data["matches"])
    dates = pd.to_datetime(
        matches["date"],
        format="%Y-%m-%d",
        errors="coerce",
    )

    invalid = dates.isna()
    outside = (dates < start) | (dates >= end)

    print(
        f"{season}: "
        f"invalid dates={invalid.sum()}, "
        f"outside season={outside.sum()}"
    )