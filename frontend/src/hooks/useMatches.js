import { useEffect, useState } from 'react'
import { getMatches } from '../api/matchesApi'

export function useMatches() {
    const [matches, setMatches] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        const controller = new AbortController()

        async function loadMatches() {
            try {
                const data = await getMatches({
                    signal: controller.signal,
                })

                if (!controller.signal.aborted) {
                    setMatches(data)
                }
            } catch (error) {
                if (!controller.signal.aborted) {
                    setError(error.message)
                }
            } finally {
                if (!controller.signal.aborted) {
                    setIsLoading(false)
                }
            }
        }

        loadMatches()

        return () => controller.abort()
    }, [])

    return { matches, isLoading, error }
}