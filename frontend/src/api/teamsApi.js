import { getJson } from './httpClient'

export function getTeams({ signal } = {}) {
    return getJson('/api/teams', { signal })
}

export function getTeamById(id, { signal } = {}) {
    return getJson(
        `/api/teams/${encodeURIComponent(id)}`,
        { signal }
    )
}

export function getTeamMatches(id, { signal } = {}) {
    return getJson(
        `/api/teams/${encodeURIComponent(id)}/matches`,
        { signal }
    )
}