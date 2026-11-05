import { getJson } from './httpClient'

export function getMatches({ signal } = {}) {
    return getJson('/api/matches', { signal })
}