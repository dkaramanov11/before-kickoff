import { useState } from 'react'
import { useMatches } from '../../hooks/useMatches'
import MatchWeekPicker from './MatchWeekPicker'
import MatchCard from './MatchCard'
import '../../styles/matches/MatchesList.css'

function MatchesList({ initialDate }) {
    const [selectedDate, setSelectedDate] = useState(initialDate)

    const { matches, isLoading, error } = useMatches({
        dateFrom: selectedDate,
        dateTo: selectedDate,
    })

    return (
        <section
            id="matches"
            className="matches"
            aria-labelledby="matches-heading"
            aria-busy={isLoading}
        >
            <h2 id="matches-heading">Matches</h2>

            <MatchWeekPicker
                selectedDate={selectedDate}
                onDateChange={setSelectedDate}
            />

            {isLoading ? (
                <p className="matches__message" role="status">
                    Loading matches...
                </p>
            ) : error ? (
                <p className="matches__message" role="alert">
                    Unable to load matches. Please try again later.
                </p>
            ) : matches.length === 0 ? (
                <p className="matches__message">
                    No matches scheduled for this date.
                </p>
            ) : (
                <ul className="matches__list">
                    {matches.map((match) => (
                        <li key={match.id}>
                            <MatchCard match={match} />
                        </li>
                    ))}
                </ul>
            )}
        </section>
    )
}

export default MatchesList