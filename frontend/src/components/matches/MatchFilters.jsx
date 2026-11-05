import { useState } from 'react'
import { useTeams } from '../../hooks/useTeams'
import '../../styles/matches/MatchFilters.css'

const emptyFilters = {
    teamId: '',
    status: '',
    dateFrom: '',
    dateTo: '',
}

function MatchFilters({ onApply }) {
    const [filters, setFilters] = useState(emptyFilters)
    const [validationError, setValidationError] = useState('')
    const { teams, isLoading, error } = useTeams()

    function handleChange(event) {
        const { name, value } = event.target

        setFilters((previous) => ({
            ...previous,
            [name]: value,
        }))

        setValidationError('')
    }

    function handleSubmit(event) {
        event.preventDefault()

        if (
            filters.dateFrom &&
            filters.dateTo &&
            filters.dateFrom > filters.dateTo
        ) {
            setValidationError('From date must be before or equal to To date.')
            return
        }

        setValidationError('')
        onApply({ ...filters })
    }

    function handleClear() {
        setFilters(emptyFilters)
        setValidationError('')
        onApply({ ...emptyFilters })
    }

    return (
        <form className="match-filters" onSubmit={handleSubmit}>
            <div className="match-filters__fields">
                <label className="match-filters__field">
                    <span>Team</span>

                    <select
                        name="teamId"
                        value={filters.teamId}
                        onChange={handleChange}
                        disabled={isLoading || Boolean(error)}
                    >
                        <option value="">
                            {isLoading ? 'Loading teams...' : 'All teams'}
                        </option>

                        {teams.map((team) => (
                            <option key={team.id} value={team.id}>
                                {team.shortName || team.name}
                            </option>
                        ))}
                    </select>
                </label>

                <label className="match-filters__field">
                    <span>Status</span>

                    <select
                        name="status"
                        value={filters.status}
                        onChange={handleChange}
                    >
                        <option value="">All statuses</option>
                        <option value="SCHEDULED">Scheduled</option>
                        <option value="TIMED">Timed</option>
                        <option value="IN_PLAY">In play</option>
                        <option value="PAUSED">Paused</option>
                        <option value="FINISHED">Finished</option>
                        <option value="POSTPONED">Postponed</option>
                        <option value="SUSPENDED">Suspended</option>
                        <option value="CANCELLED">Cancelled</option>
                        <option value="AWARDED">Awarded</option>
                    </select>
                </label>

                <label className="match-filters__field">
                    <span>From</span>

                    <input
                        type="date"
                        name="dateFrom"
                        value={filters.dateFrom}
                        onChange={handleChange}
                    />
                </label>

                <label className="match-filters__field">
                    <span>To</span>

                    <input
                        type="date"
                        name="dateTo"
                        value={filters.dateTo}
                        onChange={handleChange}
                    />
                </label>
            </div>

            {error && (
                <p className="match-filters__message" role="alert">
                    Team filter is unavailable. Other filters can still be used.
                </p>
            )}

            {validationError && (
                <p className="match-filters__message" role="alert">
                    {validationError}
                </p>
            )}

            <div className="match-filters__actions">
                <button className="match-filters__apply" type="submit">
                    Apply filters
                </button>

                <button
                    className="match-filters__clear"
                    type="button"
                    onClick={handleClear}
                >
                    Clear filters
                </button>
            </div>
        </form>
    )
}

export default MatchFilters