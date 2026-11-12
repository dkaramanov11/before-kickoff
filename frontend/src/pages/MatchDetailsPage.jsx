import { Link, useParams } from 'react-router-dom'
import { useMatchDetails } from '../hooks/useMatchDetails'
import { formatMatchDateTime } from '../utils/dateTime'
import '../styles/matches/MatchDetails.css'

function MatchDetailsPage() {
    const { id } = useParams()
    const { match, isLoading, error } = useMatchDetails(id)

    return (
        <section className="match-details" aria-busy={isLoading}>
            <Link className="match-details__back" to="/">
                ← Back to matches
            </Link>

            <h1>Match details</h1>

            {isLoading ? (
                <p role="status">Loading match details...</p>
            ) : error ? (
                <p role="alert">{error}</p>
            ) : match ? (
                <div className="match-details__card">
                    <p className="match-details__status">{match.status}</p>

                    <div className="match-details__fixture">
                        <div className="match-details__team">
                            {match.homeTeam.crestUrl && (
                                <img
                                    key={match.homeTeam.crestUrl}
                                    src={match.homeTeam.crestUrl}
                                    alt=""
                                    onError={(event) => {
                                        event.currentTarget.hidden = true
                                    }}
                                />
                            )}

                            <h2>{match.homeTeam.shortName || match.homeTeam.name}</h2>
                            <span>Home</span>
                        </div>

                        <p className="match-details__score">
                            {match.homeScore != null && match.awayScore != null
                                ? `${match.homeScore} – ${match.awayScore}`
                                : 'vs'}
                        </p>

                        <div className="match-details__team">
                            {match.awayTeam.crestUrl && (
                                <img
                                    key={match.awayTeam.crestUrl}
                                    src={match.awayTeam.crestUrl}
                                    alt=""
                                    onError={(event) => {
                                        event.currentTarget.hidden = true
                                    }}
                                />
                            )}

                            <h2>{match.awayTeam.shortName || match.awayTeam.name}</h2>
                            <span>Away</span>
                        </div>
                    </div>

                    <div className="match-details__meta">
                        <time dateTime={match.kickoffAt}>
                            {formatMatchDateTime(match.kickoffAt)}
                        </time>

                        {match.matchday != null && (
                            <span>Matchday {match.matchday}</span>
                        )}
                    </div>
                </div>
            ) : null}
        </section>
    )
}

export default MatchDetailsPage