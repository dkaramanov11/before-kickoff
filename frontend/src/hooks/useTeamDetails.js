import { useCallback, useEffect, useState } from 'react'
import { getTeamById } from '../api/teamsApi'

function getErrorMessage(status) {
    if (status === 400) {
        return 'Invalid team ID.'
    }

    if (status === 404) {
        return 'Team not found.'
    }

    return 'Unable to load team details. Please try again.'
}

export function useTeamDetails(id) {
    const [team, setTeam] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)
    const [requestVersion, setRequestVersion] = useState(0)

    useEffect(() => {
        const controller = new AbortController()

        async function loadTeam() {
            setTeam(null)
            setError(null)
            setIsLoading(true)

            try {
                const response = await getTeamById(id, {
                    signal: controller.signal,
                })

                if (!controller.signal.aborted) {
                    setTeam(response)
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

        loadTeam()

        return () => controller.abort()
    }, [id, requestVersion])

    const retry = useCallback(() => {
        setRequestVersion((current) => current + 1)
    }, [])

    return {
        team,
        isLoading,
        error,
        retry,
    }
}