import hashlib
import platform
from datetime import datetime, timezone

import joblib
import numpy as np
import pandas as pd
import sklearn

from train_model import (
    BASE_DIR,
    DATA_DIR,
    FEATURE_COLUMNS,
    LABELS,
    create_model,
)


MODEL_VERSION = "v1"
MODEL_DIR = BASE_DIR / "models"
MODEL_FILE = MODEL_DIR / f"match_outcome_{MODEL_VERSION}.joblib"


if __name__ == "__main__":
    train_file = DATA_DIR / "train.csv"
    train = pd.read_csv(
        train_file,
        dtype={"target": str},
        parse_dates=["date"],
    )

    if train.empty:
        raise ValueError("Training data is empty.")

    if train[FEATURE_COLUMNS + ["target", "date"]].isna().any().any():
        raise ValueError("Training data contains missing values.")

    if set(train["target"]) != set(LABELS):
        raise ValueError("Training data must contain all three outcomes.")

    if MODEL_FILE.exists():
        raise FileExistsError(
            f"Model already exists: {MODEL_FILE}. "
            "Use a new version to save another model."
        )

    X_train = train[FEATURE_COLUMNS]
    y_train = train["target"]

    model = create_model(c_value=100.0)
    model.fit(X_train, y_train)

    metadata = {
        "model_version": MODEL_VERSION,
        "model_type": "LogisticRegression",
        "created_at": datetime.now(timezone.utc).isoformat(),
        "feature_columns": FEATURE_COLUMNS,
        "classes": model.classes_.tolist(),
        "parameters": {
            "C": 100.0,
            "solver": "lbfgs",
            "max_iter": 1000,
            "class_weight": None,
        },
        "history_policy": {
            "matches": 5,
            "lookback_days": 365,
            "strictly_before_match_date": True,
            "insufficient_history": "no_prediction",
        },
        "training_rows": len(train),
        "training_seasons": sorted(train["season"].unique().tolist()),
        "training_date_min": train["date"].min().date().isoformat(),
        "training_date_max": train["date"].max().date().isoformat(),
        "training_file_sha256": hashlib.sha256(
            train_file.read_bytes()
        ).hexdigest(),
        "versions": {
            "python": platform.python_version(),
            "scikit_learn": sklearn.__version__,
            "pandas": pd.__version__,
            "numpy": np.__version__,
            "joblib": joblib.__version__,
        },
    }

    artifact = {
        "pipeline": model,
        "metadata": metadata,
    }

    MODEL_DIR.mkdir(parents=True, exist_ok=True)
    joblib.dump(artifact, MODEL_FILE)

    # Verify that saving and loading preserves predictions.
    loaded = joblib.load(MODEL_FILE)
    sample = X_train.head(10)

    np.testing.assert_allclose(
        model.predict_proba(sample),
        loaded["pipeline"].predict_proba(sample),
        rtol=0,
        atol=1e-12,
    )

    assert loaded["metadata"]["feature_columns"] == FEATURE_COLUMNS
    assert loaded["pipeline"].classes_.tolist() == metadata["classes"]

    print("Model version:", MODEL_VERSION)
    print("Training matches:", len(train))
    print("Features:", len(FEATURE_COLUMNS))
    print("Classes:", metadata["classes"])
    print("Saved to:", MODEL_FILE)
    print("Model reload checks passed.")