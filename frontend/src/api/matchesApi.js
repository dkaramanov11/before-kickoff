import { getJson } from './httpClient'

export function getMatches({
                               teamId = '',
                               status = '',
                               dateFrom = '',
                               dateTo = '',
                               signal,
                           } = {}) {
    const params = new URLSearchParams()

    if (teamId !== '') {
        params.set('teamId', teamId)
    }

    if (status !== '') {
        params.set('status', status)
    }

    if (dateFrom !== '') {
        params.set('dateFrom', dateFrom)
    }

    if (dateTo !== '') {
        params.set('dateTo', dateTo)
    }

    const query = params.toString()
    const path = query ? `/api/matches?${query}` : '/api/matches'

    return getJson(path, { signal })
}

export function getMatchById(id, { signal } = {}) {
    return getJson(`/api/matches/${encodeURIComponent(id)}`, { signal })
}

export function getMatchHistory(id, { signal } = {}) {
    return getJson(
        `/api/matches/${encodeURIComponent(id)}/history`,
        { signal }
    )
}