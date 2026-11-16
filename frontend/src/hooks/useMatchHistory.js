import { useCallback, useEffect, useState } from 'react'
import { getMatchHistory } from '../api/matchesApi'

function getErrorMessage(status) {
    if (status === 400) {
        return 'Invalid match ID.'
    }

    if (status === 404) {
        return 'Match not found.'
    }

    return 'Unable to load match history. Please try again.'
}

export function useMatchHistory(id) {
    const [history, setHistory] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)
    const [requestVersion, setRequestVersion] = useState(0)

    useEffect(() => {
        const controller = new AbortController()

        async function loadHistory() {
            setIsLoading(true)
            setError(null)
            setHistory(null)

            try {
                const response = await getMatchHistory(id, {
                    signal: controller.signal,
                })

                if (!controller.signal.aborted) {
                    setHistory(response)
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

        loadHistory()

        return () => controller.abort()
    }, [id, requestVersion])

    const retry = useCallback(() => {
        setRequestVersion((current) => current + 1)
    }, [])

    return {
        history,
        isLoading,
        error,
        retry,
    }
}