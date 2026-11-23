from contextlib import asynccontextmanager
from pathlib import Path

import joblib
from fastapi import FastAPI, HTTPException, Request

import numpy as np
import pandas as pd

from app.schemas import PredictionRequest

BASE_DIR = Path(__file__).resolve().parent.parent
MODEL_FILE = BASE_DIR / "models" / "match_outcome_v1.joblib"


@asynccontextmanager
async def lifespan(app: FastAPI):
    if not MODEL_FILE.is_file():
        raise FileNotFoundError(f"Model file not found: {MODEL_FILE}")

    artifact = joblib.load(MODEL_FILE)

    pipeline = artifact["pipeline"]
    metadata = artifact["metadata"]

    if set(pipeline.classes_) != {"1", "X", "2"}:
        raise ValueError("Model must support outcomes 1, X and 2.")

    if list(pipeline.feature_names_in_) != metadata["feature_columns"]:
        raise ValueError("Model features do not match metadata.")

    if pipeline.classes_.tolist() != metadata["classes"]:
        raise ValueError("Model classes do not match metadata.")

    app.state.model_artifact = artifact

    try:
        yield
    finally:
        app.state.model_artifact = None


app = FastAPI(
    title="Before Kickoff ML Service",
    version="1.0.0",
    lifespan=lifespan,
)


@app.get("/health")
def health():
    return {
        "status": "ok",
        "service": "before-kickoff-ml",
    }


@app.get("/ready")
def ready(request: Request):
    artifact = getattr(request.app.state, "model_artifact", None)

    if artifact is None:
        raise HTTPException(
            status_code=503,
            detail="Model is not loaded.",
        )

    return {
        "status": "ready",
        "model_version": artifact["metadata"]["model_version"],
        "classes": artifact["metadata"]["classes"],
        "feature_count": len(artifact["metadata"]["feature_columns"]),
    }

@app.post("/predict")
def predict(payload: PredictionRequest, request: Request):
    artifact = getattr(request.app.state, "model_artifact", None)

    if artifact is None:
        raise HTTPException(
            status_code=503,
            detail="Model is not loaded.",
        )

    pipeline = artifact["pipeline"]
    metadata = artifact["metadata"]
    feature_columns = metadata["feature_columns"]
    features = payload.model_dump()

    if set(features) != set(feature_columns):
        raise HTTPException(
            status_code=500,
            detail="Model feature configuration mismatch.",
        )

    inputs = pd.DataFrame(
        [features],
        columns=feature_columns,
    )

    probabilities = pipeline.predict_proba(inputs)[0]

    if (
            not np.isfinite(probabilities).all()
            or (probabilities < 0).any()
            or (probabilities > 1).any()
            or not np.isclose(probabilities.sum(), 1.0)
    ):
        raise HTTPException(
            status_code=500,
            detail="Model returned invalid probabilities.",
        )

    probabilities_by_class = {
        str(label): float(probability)
        for label, probability in zip(
            pipeline.classes_,
            probabilities,
        )
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