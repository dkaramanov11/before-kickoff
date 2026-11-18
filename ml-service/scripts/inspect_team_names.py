import json
from pathlib import Path


BASE_DIR = Path(__file__).resolve().parent.parent
RAW_DIR = BASE_DIR / "data" / "raw"

team_seasons = {}

for file_path in sorted(RAW_DIR.glob("premier-league-*.json")):
    season = file_path.stem.removeprefix("premier-league-")

    with file_path.open(encoding="utf-8") as file:
        data = json.load(file)

    for match in data["matches"]:
        for team_name in (match["team1"], match["team2"]):
            if team_name not in team_seasons:
                team_seasons[team_name] = set()

            team_seasons[team_name].add(season)

print("Distinct source team names:", len(team_seasons))
print()

for team_name, seasons in sorted(team_seasons.items()):
    print(f"{team_name}: {', '.join(sorted(seasons))}")

    mapping_file = BASE_DIR / "data" / "mappings" / "team_aliases.json"

with mapping_file.open(encoding="utf-8") as file:
    aliases = json.load(file)

canonical_names = {
    aliases.get(team_name, team_name)
    for team_name in team_seasons
}

print()
print("Distinct canonical teams:", len(canonical_names))