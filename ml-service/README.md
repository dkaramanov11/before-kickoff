# Before Kickoff — ML

Machine learning component for predicting Premier League match outcomes:
home win (1), draw (X), and away win (2).

## Current status

Python environment and training dependencies are set up.
Data preparation, model training, and the FastAPI service are not implemented yet.

## Requirements

- Python 3.14.4 — used for the initial setup.
- Windows PowerShell — for the commands below.

## Local setup

Run these commands from the repository root:

```powershell
cd ml-service
py -3.14 -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
```

## Verify installation

```powershell
python -m pip check
python -c "import pandas; import sklearn; print('pandas:', pandas.__version__); print('scikit-learn:', sklearn.__version__)"
```

## Daily use

From the ml-service directory, activate the environment:

```powershell
.\.venv\Scripts\Activate.ps1
```

Deactivate it when finished:

```powershell
deactivate
```

The .venv directory is local and excluded from Git.
Dependency versions are recorded in requirements.txt.