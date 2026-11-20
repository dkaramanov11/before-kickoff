from pathlib import Path

import pandas as pd
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score, log_loss
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler


BASE_DIR = Path(__file__).resolve().parent.parent
DATA_DIR = BASE_DIR / "data" / "processed"

FEATURE_COLUMNS = [
    "home_points_last5",
    "away_points_last5",
    "home_goals_for_avg_last5",
    "away_goals_for_avg_last5",
    "home_goals_against_avg_last5",
    "away_goals_against_avg_last5",
]


if __name__ == "__main__":
    train = pd.read_csv(
        DATA_DIR / "train.csv",
        dtype={"target": str},
        )
    validation = pd.read_csv(
        DATA_DIR / "validation.csv",
        dtype={"target": str},
        )

    for name, data in [("train", train), ("validation", validation)]:
        if data.empty:
            raise ValueError(f"{name} is empty.")

        if data[FEATURE_COLUMNS + ["target"]].isna().any().any():
            raise ValueError(f"{name} contains missing values.")

        if not data["target"].isin(["1", "X", "2"]).all():
            raise ValueError(f"{name} contains invalid targets.")

    if set(train["target"]) != {"1", "X", "2"}:
        raise ValueError("Training data must contain all three outcomes.")

    X_train = train[FEATURE_COLUMNS]
    y_train = train["target"]

    X_validation = validation[FEATURE_COLUMNS]
    y_validation = validation["target"]

    model = Pipeline([
        ("scaler", StandardScaler()),
        ("classifier", LogisticRegression(
            solver="lbfgs",
            C=1.0,
            max_iter=1000,
        )),
    ])

    model.fit(X_train, y_train)

    predictions = model.predict(X_validation)
    probabilities = model.predict_proba(X_validation)

    accuracy = accuracy_score(y_validation, predictions)
    loss = log_loss(
        y_validation,
        probabilities,
        labels=model.classes_,
    )

    print("Model: Logistic Regression")
    print("Training matches:", len(train))
    print("Validation matches:", len(validation))
    print("Features:", len(FEATURE_COLUMNS))
    print()
    print(f"Validation accuracy: {accuracy:.4f}")
    print(f"Validation log loss: {loss:.4f}")
    print()
    print("Predicted outcomes:")
    print(
        pd.Series(predictions)
        .value_counts()
        .reindex(["1", "X", "2"], fill_value=0)
        .to_string()
    )