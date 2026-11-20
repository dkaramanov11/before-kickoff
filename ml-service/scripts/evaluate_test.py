from math import log

import pandas as pd
from sklearn.metrics import (
    accuracy_score,
    classification_report,
    confusion_matrix,
    log_loss,
)

from train_model import DATA_DIR, FEATURE_COLUMNS, LABELS, create_model


if __name__ == "__main__":
    train = pd.read_csv(
        DATA_DIR / "train.csv",
        dtype={"target": str},
        parse_dates=["date"],
        )
    test = pd.read_csv(
        DATA_DIR / "test.csv",
        dtype={"target": str},
        parse_dates=["date"],
        )

    for name, data in [("train", train), ("test", test)]:
        if data.empty:
            raise ValueError(f"{name} is empty.")

        required_columns = FEATURE_COLUMNS + ["target", "date"]

        if data[required_columns].isna().any().any():
            raise ValueError(f"{name} contains missing values.")

        if not data["target"].isin(LABELS).all():
            raise ValueError(f"{name} contains invalid targets.")

    if set(train["target"]) != set(LABELS):
        raise ValueError("Training data must contain all three outcomes.")

    if set(test["season"]) != {"2024-25"}:
        raise ValueError("Test data must contain only season 2024-25.")

    if train["date"].max() >= test["date"].min():
        raise ValueError("Training dates must precede test dates.")

    X_train = train[FEATURE_COLUMNS]
    y_train = train["target"]

    X_test = test[FEATURE_COLUMNS]
    y_test = test["target"]

    model = create_model(c_value=100.0)
    model.fit(X_train, y_train)

    predictions = model.predict(X_test)
    probabilities = model.predict_proba(X_test)

    model_accuracy = accuracy_score(y_test, predictions)
    model_loss = log_loss(
        y_test,
        probabilities,
        labels=model.classes_,
    )

    # Learn baseline probabilities from training data only.
    baseline_probabilities = (
            y_train.value_counts()
            .reindex(LABELS, fill_value=0)
            / len(y_train)
    )
    baseline_target = baseline_probabilities.idxmax()

    baseline_accuracy = (y_test == baseline_target).mean()
    baseline_loss = -sum(
        log(float(baseline_probabilities[target]))
        for target in y_test
    ) / len(y_test)

    comparison = pd.DataFrame([
        {
            "model": "Frequency baseline",
            "accuracy": baseline_accuracy,
            "log_loss": baseline_loss,
        },
        {
            "model": "Logistic Regression C=100",
            "accuracy": model_accuracy,
            "log_loss": model_loss,
        },
    ])

    print("Final test evaluation: 2024-25")
    print("Training matches:", len(train))
    print("Test matches:", len(test))
    print()
    print(comparison.to_string(
        index=False,
        float_format="%.6f",
    ))

    matrix = confusion_matrix(
        y_test,
        predictions,
        labels=LABELS,
    )

    print()
    print("Confusion matrix:")
    print(pd.DataFrame(
        matrix,
        index=[f"actual_{label}" for label in LABELS],
        columns=[f"predicted_{label}" for label in LABELS],
    ).to_string())

    print()
    print("Classification report:")
    print(classification_report(
        y_test,
        predictions,
        labels=LABELS,
        digits=4,
        zero_division=0,
    ))