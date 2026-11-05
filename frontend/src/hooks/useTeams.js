import { useEffect, useState } from 'react'
import { getTeams } from '../api/teamsApi'

export function useTeams() {
  const [teams, setTeams] = useState([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    const controller = new AbortController()

    async function loadTeams() {
      try {
        const data = await getTeams({
          signal: controller.signal,
        })

        if (!controller.signal.aborted) {
          setTeams(data)
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

    loadTeams()

    return () => controller.abort()
  }, [])

  return { teams, isLoading, error }
}