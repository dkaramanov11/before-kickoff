from math import log
from pathlib import Path

import pandas as pd


BASE_DIR = Path(__file__).resolve().parent.parent
DATA_DIR = BASE_DIR / "data" / "processed"
TARGETS = ["1", "X", "2"]


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

        if not data["target"].isin(TARGETS).all():
            raise ValueError(f"{name} contains invalid targets.")

    counts = train["target"].value_counts().reindex(
        TARGETS, fill_value=0
    )

    if (counts == 0).any():
        raise ValueError("Training data must contain all three outcomes.")

    probabilities = counts / len(train)
    predicted_target = probabilities.idxmax()

    accuracy = (
            validation["target"] == predicted_target
    ).mean()

    log_loss = -sum(
        log(float(probabilities[target]))
        for target in validation["target"]
    ) / len(validation)

    print("Baseline probabilities learned from train:")

    for target in TARGETS:
        print(f"{target}: {probabilities[target]:.4f}")

    print()
    print("Predicted outcome for every match:", predicted_target)
    print("Validation matches:", len(validation))
    print(f"Validation accuracy: {accuracy:.4f}")
    print(f"Validation log loss: {log_loss:.4f}")