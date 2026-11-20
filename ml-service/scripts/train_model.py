from pathlib import Path

import pandas as pd
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import (
    accuracy_score,
    classification_report,
    confusion_matrix,
    log_loss,
)
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

LABELS = ["1", "X", "2"]
C_VALUES = [0.01, 0.1, 1.0, 10.0, 100.0]


def create_model(c_value):
    return Pipeline([
        ("scaler", StandardScaler()),
        ("classifier", LogisticRegression(
            solver="lbfgs",
            C=c_value,
            max_iter=1000,
        )),
    ])


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

        if not data["target"].isin(LABELS).all():
            raise ValueError(f"{name} contains invalid targets.")

    if set(train["target"]) != set(LABELS):
        raise ValueError("Training data must contain all three outcomes.")

    X_train = train[FEATURE_COLUMNS]
    y_train = train["target"]

    X_validation = validation[FEATURE_COLUMNS]
    y_validation = validation["target"]

    # Evaluate the initial model.
    model = create_model(c_value=100.0)
    model.fit(X_train, y_train)

    predictions = model.predict(X_validation)
    probabilities = model.predict_proba(X_validation)

    accuracy = accuracy_score(y_validation, predictions)
    loss = log_loss(
        y_validation,
        probabilities,
        labels=model.classes_,
    )

    print("Model: Logistic Regression (C=100.0)")
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
        .reindex(LABELS, fill_value=0)
        .to_string()
    )

    matrix = confusion_matrix(
        y_validation,
        predictions,
        labels=LABELS,
    )

    matrix_table = pd.DataFrame(
        matrix,
        index=[f"actual_{label}" for label in LABELS],
        columns=[f"predicted_{label}" for label in LABELS],
    )

    print()
    print("Confusion matrix:")
    print(matrix_table.to_string())

    print()
    print("Classification report:")
    print(classification_report(
        y_validation,
        predictions,
        labels=LABELS,
        digits=4,
        zero_division=0,
    ))

    # Compare regularization values using validation data only.
    results = []

    for c_value in C_VALUES:
        candidate = create_model(c_value)
        candidate.fit(X_train, y_train)

        candidate_predictions = candidate.predict(X_validation)
        candidate_probabilities = candidate.predict_proba(X_validation)

        results.append({
            "C": c_value,
            "accuracy": accuracy_score(
                y_validation,
                candidate_predictions,
            ),
            "log_loss": log_loss(
                y_validation,
                candidate_probabilities,
                labels=candidate.classes_,
            ),
            "predicted_draws": int(
                (candidate_predictions == "X").sum()
            ),
        })

    comparison = (
        pd.DataFrame(results)
        .sort_values(["log_loss", "C"])
        .reset_index(drop=True)
    )

    print()
    print("Regularization comparison:")
    print(comparison.to_string(
        index=False,
        float_format="%.6f",
    ))