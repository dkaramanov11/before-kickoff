import { Link, useParams } from 'react-router-dom'
import { useMatchDetails } from '../hooks/useMatchDetails'
import MatchHistory from '../components/matches/MatchHistory'
import MatchStatus from '../components/matches/MatchStatus'
import StandingsTable from '../components/standings/StandingsTable'
import TeamCrest from '../components/teams/TeamCrest'
import MatchPrediction from '../components/matches/MatchPrediction'
import {
    formatMatchDateTime,
    formatKickoffTime,
} from '../utils/dateTime'
import '../styles/matches/MatchDetails.css'

function MatchDetailsPage() {
    const { id } = useParams()
    const { match, isLoading, error } = useMatchDetails(id)

    const isUpcoming =
        match?.status === 'SCHEDULED' || match?.status === 'TIMED'

    const hasScore =
        match != null &&
        match.homeScore != null &&
        match.awayScore != null

    const result = !match
        ? null
        : isUpcoming
            ? formatKickoffTime(match.kickoffAt)
            : hasScore
                ? `${match.homeScore} – ${match.awayScore}`
                : 'vs'

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
                <>
                    <div className="match-details__card">
                        <div className="match-details__status">
                            <MatchStatus status={match.status} />
                        </div>

                        <div className="match-details__fixture">
                            <div className="match-details__team">
                                <TeamCrest url={match.homeTeam.crestUrl} />

                                <h2>
                                    {match.homeTeam.shortName ??
                                        match.homeTeam.name}
                                </h2>

                                <span>Home</span>
                            </div>

                            <p className="match-details__score">
                                {result}
                            </p>

                            <div className="match-details__team">
                                <TeamCrest url={match.awayTeam.crestUrl} />

                                <h2>
                                    {match.awayTeam.shortName ??
                                        match.awayTeam.name}
                                </h2>

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

                    <MatchPrediction key={match.id} match={match} />

                    <div className="match-details__content">
                        <MatchHistory key={match.id} match={match} />

                        <StandingsTable
                            title="Current standings"
                            homeTeamId={match.homeTeam.id}
                            awayTeamId={match.awayTeam.id}
                        />
                    </div>
                </>
            ) : null}
        </section>
    )
}

export default MatchDetailsPage