from pydantic import BaseModel, ConfigDict, Field


class PredictionRequest(BaseModel):
    model_config = ConfigDict(
        extra="forbid",
        strict=True,
        allow_inf_nan=False,
    )

    home_points_last5: int = Field(ge=0, le=15)
    away_points_last5: int = Field(ge=0, le=15)

    home_goals_for_avg_last5: float = Field(ge=0)
    away_goals_for_avg_last5: float = Field(ge=0)

    home_goals_against_avg_last5: float = Field(ge=0)
    away_goals_against_avg_last5: float = Field(ge=0)