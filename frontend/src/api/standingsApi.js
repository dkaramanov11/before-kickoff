import { getJson } from './httpClient'

export function getPremierLeagueStandings({ signal } = {}) {
    return getJson('/api/standings/premier-league', { signal })
}