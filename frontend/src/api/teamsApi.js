import { getJson } from './httpClient'

export function getTeams({ signal } = {}) {
    return getJson('/api/teams', { signal })
}