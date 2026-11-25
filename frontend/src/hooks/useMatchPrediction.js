import { useCallback, useEffect, useState } from 'react'
import { getMatchPrediction } from '../api/matchesApi'

function getErrorMessage(status) {
    if (status === 400) {
        return 'Prediction is not available for this match.'
    }

    if (status === 404) {
        return 'Match not found.'
    }

    return 'Unable to load prediction. Please try again.'
}

export function useMatchPrediction(id) {
    const [prediction, setPrediction] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [error, setError] = useState(null)
    const [requestVersion, setRequestVersion] = useState(0)

    useEffect(() => {
        const controller = new AbortController()

        async function loadPrediction() {
            setPrediction(null)
            setError(null)
            setIsLoading(true)

            try {
                const response = await getMatchPrediction(id, {
                    signal: controller.signal,
                })

                if (controller.signal.aborted) return

                if (
                    response.status === 'ok' ||
                    response.status === 'insufficient_history'
                ) {
                    setPrediction(response)
                } else {
                    setError('Unexpected prediction response. Please try again.')
                }
            } catch (error) {
                if (controller.signal.aborted) return

                if (
                    error.status === 503 &&
                    error.body?.status === 'prediction_unavailable'
                ) {
                    setPrediction(error.body)
                } else {
                    setError(getErrorMessage(error.status))
                }
            } finally {
                if (!controller.signal.aborted) {
                    setIsLoading(false)
                }
            }
        }

        loadPrediction()

        return () => controller.abort()
    }, [id, requestVersion])

    const retry = useCallback(() => {
        setRequestVersion((current) => current + 1)
    }, [])

    return {
        prediction,
        isLoading,
        error,
        retry,
    }
}