import { useMatches } from '../../hooks/useMatches'
import { formatMatchDateTime } from '../../utils/dateTime'
import { useState } from 'react'
import MatchFilters from './MatchFilters'
import '../../styles/matches/MatchesList.css'

function MatchesList() {
    const [appliedFilters, setAppliedFilters] = useState({})
    const { matches, isLoading, error } = useMatches(appliedFilters)

    return (
        <section
            id="matches"
            className="matches"
            aria-labelledby="matches-heading"
            aria-busy={isLoading}
        >
            <h2 id="matches-heading">Matches</h2>
            <MatchFilters onApply={setAppliedFilters} />

            {isLoading ? (
                <p className="matches__message" role="status">
                    Loading matches...
                </p>
            ) : error ? (
                <p className="matches__message" role="alert">
                    Unable to load matches. Please try again later.
                </p>
            ) : matches.length === 0 ? (
                <p className="matches__message">No matches found.</p>
            ) : (
                <ul className="matches__list">
                    {matches.map((match) => {
                        const hasScore =
                            match.homeScore != null && match.awayScore != null

                        return (
                            <li key={match.id} className="matches__item">
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

                                {match.matchday != null && (
                                    <p className="matches__matchday">
                                        Matchday {match.matchday}
                                    </p>
                                )}
                            </li>
                        )
                    })}
                </ul>
            )}
        </section>
    )
}

export default MatchesList