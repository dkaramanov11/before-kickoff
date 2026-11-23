from contextlib import asynccontextmanager
from pathlib import Path

import joblib
from fastapi import FastAPI, HTTPException, Request


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