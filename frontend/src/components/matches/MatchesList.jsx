import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useMatches } from '../../hooks/useMatches'
import { formatMatchDateTime } from '../../utils/dateTime'
import MatchWeekPicker from './MatchWeekPicker'
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
                    {matches.map((match) => {
                        const hasScore =
                            match.homeScore != null && match.awayScore != null

                        return (
                            <li key={match.id} className="matches__item">
                                <Link
                                    className="matches__link"
                                    to={`/matches/${match.id}`}
                                >
                                    <div className="matches__meta">
                                        <time dateTime={match.kickoffAt}>
                                            {formatMatchDateTime(match.kickoffAt)}
                                        </time>

                                        <span>{match.status || 'Unknown status'}</span>
                                    </div>

                                    <div className="matches__fixture">
                    <span className="matches__home">
                      {match.homeTeam.shortName || match.homeTeam.name}
                    </span>

                                        <span className="matches__score">
                      {hasScore
                          ? `${match.homeScore} – ${match.awayScore}`
                          : 'vs'}
                    </span>

                                        <span className="matches__away">
                      {match.awayTeam.shortName || match.awayTeam.name}
                    </span>
                                    </div>

                                    <div className="matches__footer">
                    <span className="matches__matchday">
                      {match.matchday != null
                          ? `Matchday ${match.matchday}`
                          : ''}
                    </span>

                                        <span className="matches__details">
                      View details <span aria-hidden="true">→</span>
                    </span>
                                    </div>
                                </Link>
                            </li>
                        )
                    })}
                </ul>
            )}
        </section>
    )
}

export default MatchesList