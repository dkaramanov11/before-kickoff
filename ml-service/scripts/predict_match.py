import json
from pathlib import Path

import joblib
import numpy as np
import pandas as pd

from inspect_match_features import build_match_features


BASE_DIR = Path(__file__).resolve().parent.parent
MODEL_FILE = BASE_DIR / "models" / "match_outcome_v1.joblib"
DATA_FILE = BASE_DIR / "data" / "processed" / "matches.csv"


def predict_match(artifact, matches, home_team, away_team, match_date):
    features = build_match_features(
        matches,
        home_team,
        away_team,
        match_date,
    )

    metadata = artifact["metadata"]

    if features is None:
        return {
            "status": "insufficient_history",
            "model_version": metadata["model_version"],
            "probabilities": None,
        }

    model = artifact["pipeline"]
    feature_columns = metadata["feature_columns"]

    if set(features) != set(feature_columns):
        raise ValueError("Features do not match the saved model.")

    inputs = pd.DataFrame(
        [features],
        columns=feature_columns,
    )

    probabilities = model.predict_proba(inputs)[0]

    if not np.isfinite(probabilities).all():
        raise ValueError("Model returned invalid probabilities.")

    if (
            (probabilities < 0).any()
            or (probabilities > 1).any()
            or not np.isclose(probabilities.sum(), 1.0)
    ):
        raise ValueError("Probabilities must be between 0 and 1 and sum to 1.")

    probabilities_by_class = {
        str(label): float(probability)
        for label, probability in zip(model.classes_, probabilities)
    }

    return {
        "status": "ok",
        "model_version": metadata["model_version"],
        "predicted_outcome": max(
            probabilities_by_class,
            key=probabilities_by_class.get,
        ),
        "probabilities": {
            "1": probabilities_by_class["1"],
            "X": probabilities_by_class["X"],
            "2": probabilities_by_class["2"],
        },
    }


if __name__ == "__main__":
    artifact = joblib.load(MODEL_FILE)
    matches = pd.read_csv(DATA_FILE, parse_dates=["date"])

    home_team = "Arsenal FC"
    away_team = "Wolverhampton Wanderers FC"
    match_date = "2024-08-17"

    result = predict_match(
        artifact,
        matches,
        home_team,
        away_team,
        match_date,
    )

    print("Home team:", home_team)
    print("Away team:", away_team)
    print("Match date:", match_date)
    print()
    print(json.dumps(result, indent=2))

    assert result["status"] == "ok"

    # Removing the selected match and all later matches
    # must not change the prediction.
    past_only = matches.loc[
        matches["date"] < pd.Timestamp(match_date)
        ]

    result_from_past = predict_match(
        artifact,
        past_only,
        home_team,
        away_team,
        match_date,
    )

    assert result == result_from_past

    # Ipswich has insufficient recent Premier League history.
    insufficient_history = predict_match(
        artifact,
        matches,
        "Ipswich Town FC",
        "Liverpool FC",
        "2024-08-17",
    )

    assert insufficient_history["status"] == "insufficient_history"
    assert insufficient_history["probabilities"] is None

    print()
    print("Prediction checks passed.")