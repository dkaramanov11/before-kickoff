import argparse
import json
import re
from pathlib import Path
from urllib.error import HTTPError, URLError
from urllib.request import urlopen


parser = argparse.ArgumentParser(
    description="Download Premier League season data from OpenFootball."
)
parser.add_argument("--season", required=True)
args = parser.parse_args()

if not re.fullmatch(r"\d{4}-\d{2}", args.season):
    parser.error("Use the season format YYYY-YY, for example 2022-23.")

base_dir = Path(__file__).resolve().parent.parent
output_file = (
        base_dir / "data" / "raw" / f"premier-league-{args.season}.json"
)

if output_file.exists():
    parser.error(f"File already exists: {output_file}")

url = (
    "https://raw.githubusercontent.com/openfootball/football.json/"
    f"master/{args.season}/en.1.json"
)

try:
    with urlopen(url, timeout=30) as response:
        content = response.read()

    data = json.loads(content)

    if not isinstance(data, dict) or not isinstance(data.get("matches"), list):
        parser.error("Downloaded JSON does not contain a matches list.")

except (HTTPError, URLError, TimeoutError) as error:
    parser.exit(1, f"Download failed: {error}\n")
except (ValueError, UnicodeDecodeError) as error:
    parser.exit(1, f"Invalid JSON: {error}\n")

output_file.parent.mkdir(parents=True, exist_ok=True)
output_file.write_bytes(content)

print(f"Downloaded: {output_file}")
print(f"Matches in source: {len(data['matches'])}")
print("Run inspect_data.py to validate the season.")