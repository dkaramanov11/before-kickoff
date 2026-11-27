import { useCallback, useEffect, useState } from 'react'
import { getTeamMatches } from '../api/teamsApi'

function getErrorMessage(status) {
    if (status === 400) {
        return 'Invalid team ID.'
    }

    if (status === 404) {
        return 'Team not found.'
    }

    return 'Unable to load team matches. Please try again.'
}

export function useTeamMatches(id) {
    const [data, setData] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)
    const [requestVersion, setRequestVersion] = useState(0)

    useEffect(() => {
        const controller = new AbortController()

        async function loadMatches() {
            setData(null)
            setError(null)
            setIsLoading(true)

            try {
                const response = await getTeamMatches(id, {
                    signal: controller.signal,
                })

                if (!controller.signal.aborted) {
                    setData(response)
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

        loadMatches()

        return () => controller.abort()
    }, [id, requestVersion])

    const retry = useCallback(() => {
        setRequestVersion((current) => current + 1)
    }, [])

    return {
        data,
        isLoading,
        error,
        retry,
    }
}