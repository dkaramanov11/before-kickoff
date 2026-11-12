import { useEffect, useState } from 'react'
import { getMatches } from '../api/matchesApi'
import { getDateInSkopje, getTodayDate } from '../utils/calendar'

export function useInitialMatchDate() {
    const [initialDate, setInitialDate] = useState(null)
    const [error, setError] = useState(null)

    useEffect(() => {
        const controller = new AbortController()

        async function loadInitialDate() {
            try {
                const matches = await getMatches({
                    dateFrom: getTodayDate(),
                    signal: controller.signal,
                })

                const now = Date.now()

                const nextMatch = matches
                    .filter(
                        (match) =>
                            ['SCHEDULED', 'TIMED'].includes(match.status) &&
                            match.kickoffAt &&
                            new Date(match.kickoffAt).getTime() > now,
                    )
                    .sort(
                        (first, second) =>
                            new Date(first.kickoffAt).getTime() -
                            new Date(second.kickoffAt).getTime(),
                    )[0]

                if (!controller.signal.aborted) {
                    setInitialDate(
                        nextMatch
                            ? getDateInSkopje(nextMatch.kickoffAt)
                            : getTodayDate(),
                    )
                }
            } catch {
                if (!controller.signal.aborted) {
                    setError('Unable to find the next scheduled match.')
                }
            }
        }

        loadInitialDate()

        return () => controller.abort()
    }, [])

    return {
        initialDate,
        isLoading: initialDate === null && error === null,
        error,
    }
}