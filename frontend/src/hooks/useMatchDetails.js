import { useEffect, useState } from 'react'
import { getMatchById } from '../api/matchesApi'

function getErrorMessage(status) {
    if (status === 404) {
        return 'Match not found.'
    }

    if (status === 400) {
        return 'Invalid match ID.'
    }

    return 'Unable to load match details. Please try again later.'
}

export function useMatchDetails(id) {
    const [match, setMatch] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        const controller = new AbortController()

        async function loadMatch() {
            setIsLoading(true)
            setError(null)
            setMatch(null)

            try {
                const data = await getMatchById(id, {
                    signal: controller.signal,
                })

                if (!controller.signal.aborted) {
                    setMatch(data)
                }
            } catch (error) {
                if (!controller.signal.aborted) {
                    setError(getErrorMessage(error.status))
                }
            } finally {
                if (!controller.signal.aborted) {
                    setIsLoading(false)
                }
            }
        }

        loadMatch()

        return () => controller.abort()
    }, [id])

    return { match, isLoading, error }
}