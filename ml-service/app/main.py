from fastapi import FastAPI


app = FastAPI(
    title="Before Kickoff ML Service",
    version="1.0.0",
)


@app.get("/health")
def health():
    return {
        "status": "ok",
        "service": "before-kickoff-ml",
    }