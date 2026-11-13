import { useEffect, useState } from 'react'
import { getPremierLeagueStandings } from '../api/standingsApi'

export function useStandings() {
    const [data, setData] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)
    const [isUnavailable, setIsUnavailable] = useState(false)
    const [requestVersion, setRequestVersion] = useState(0)

    useEffect(() => {
        const controller = new AbortController()

        async function loadStandings() {
            setIsLoading(true)
            setError(null)
            setIsUnavailable(false)

            try {
                const response = await getPremierLeagueStandings({
                    signal: controller.signal,
                })

                if (!controller.signal.aborted) {
                    setData(response)
                }
            } catch (error) {
                if (!controller.signal.aborted) {
                    setData(null)

                    if (error.status === 404) {
                        setIsUnavailable(true)
                    } else {
                        setError('Unable to load standings. Please try again.')
                    }
                }
            } finally {
                if (!controller.signal.aborted) {
                    setIsLoading(false)
                }
            }
        }

        loadStandings()

        return () => controller.abort()
    }, [requestVersion])

    function retry() {
        setRequestVersion((previous) => previous + 1)
    }

    return { data, isLoading, error, isUnavailable, retry }
}