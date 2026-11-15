# Before Kickoff

> **🚧 Work in progress — actively being developed.** Core match browsing and Premier League standings are implemented. Team form comparison, machine learning predictions, and AI analysis are planned next.

**Football stats. Match predictions. Insights before kickoff.**

Before Kickoff is a full-stack football analytics application, initially focused on the Premier League. It brings match schedules, results, and league standings together, with the goal of adding an independently trained machine learning model and AI explanations grounded in real match data.

This is an ongoing portfolio project built incrementally, from importing football data and designing the database to frontend integration and, later, model evaluation and deployment.

## Current functionality

### Backend

- Spring Boot application with PostgreSQL running locally through Docker Compose.
- Database schema managed with Flyway migrations.
- Integration with football-data.org to import Premier League teams and matches.
- Imported records identified by external API IDs to support updates without duplicates.
- REST endpoints for teams, matches, and individual match details.
- Match filtering by date, team, and status using Spring Data JPA Specifications.
- Premier League standings imported into PostgreSQL and exposed through a dedicated endpoint.
- Application data served from PostgreSQL rather than fetched from the external provider on every page visit.
- Environment configuration documented through an `.env.example` file.

### Frontend

- React application built with Vite.
- Match list connected to the Spring Boot backend.
- Navigation from a match to its details page.
- Premier League standings displayed alongside the match list.
- League table showing position, team, matches played, goals for/against, and points.

The standings view uses the latest saved import and is independent of the selected match filters.

## Technology

| Layer | Technology | Status |
|---|---|---|
| Backend | Java 21, Spring Boot, Spring Data JPA, Validation | Implemented |
| Database | PostgreSQL, Flyway | Implemented |
| Local infrastructure | Docker Compose | Implemented |
| Frontend | React, Vite, CSS | Implemented |
| Machine learning | Python, pandas, scikit-learn | Planned |
| Prediction API | FastAPI | Planned |
| AI analysis | LLM integration orchestrated by Spring Boot | Planned |

## Architecture

The React frontend communicates with Spring Boot. The backend reads application data from PostgreSQL and handles imports from football-data.org. External API keys stay on the server.

The planned ML service will run separately: Spring Boot will request predictions from FastAPI and save them for later evaluation. The planned AI component will explain the available statistics and predictions.

| Component | Responsibility |
|---|---|
| React | Match browsing, details, standings, and future analysis views |
| Spring Boot | REST API, data imports, business logic, and service integration |
| PostgreSQL | Persisted teams, matches, standings, and future predictions/analyses |
| football-data.org | External source for current football data |
| FastAPI — planned | Load a trained model and return outcome probabilities |
| AI component — planned | Generate explanations grounded in retrieved data |

## Planned prediction approach

The model will estimate three outcomes: **home win (1), draw (X), and away win (2)**. It will return probabilities between 0 and 1 that sum to 1.

Initial candidate inputs include recent points, average goals scored and conceded, and home/away performance. Historical results are planned to come from OpenFootball, subject to coverage and data quality checks.

Evaluation will use chronological training, validation, and test splits. Features must use only information available before each match, and predictions will be compared against a simple baseline using accuracy and log loss. No target accuracy is claimed before evaluation.

The ML model will calculate probabilities. The AI component will explain those predictions and the underlying statistics.

## Repository structure

```text
before-kickoff/
├── backend/       # Spring Boot application
├── frontend/      # React application
├── ml-service/    # Reserved for model training and the prediction API
└── docs/          # Project documentation and integration contracts
```

## Development status

The application currently runs locally. A public demo and complete setup guide are planned as development progresses. ML predictions and AI analysis are not yet available.

The first version focuses on Premier League matches and does not include live score tracking, player lineups, or injury data.
