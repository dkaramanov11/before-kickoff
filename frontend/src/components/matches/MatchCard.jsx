import { Link } from 'react-router-dom'
import MatchTeam from './MatchTeam'
import MatchStatus from './MatchStatus'
import {
    formatMatchDateTime,
    formatKickoffTime,
} from '../../utils/dateTime'
import '../../styles/matches/MatchCard.css'

function MatchCard({ match }) {
    const isUpcoming =
        match.status === 'SCHEDULED' || match.status === 'TIMED'

    const hasScore =
        match.homeScore != null && match.awayScore != null

    const result = isUpcoming
        ? formatKickoffTime(match.kickoffAt)
        : hasScore
            ? `${match.homeScore} – ${match.awayScore}`
            : 'vs'

    const homeName = match.homeTeam.shortName ?? match.homeTeam.name
    const awayName = match.awayTeam.shortName ?? match.awayTeam.name
    const matchPath = `/matches/${match.id}`

    return (
        <article className="match-card">
            <div className="match-card__meta">
                <time dateTime={match.kickoffAt}>
                    {formatMatchDateTime(match.kickoffAt)}
                </time>

                <MatchStatus status={match.status} />
            </div>

            <div className="match-card__fixture">
                <MatchTeam team={match.homeTeam} />

                <Link
                    className="match-card__result"
                    to={matchPath}
                    aria-label={`${homeName} vs ${awayName}, ${result}, match details`}
                >
                    {result}
                </Link>

                <MatchTeam team={match.awayTeam} isAway />
            </div>

            <div className="match-card__footer">
                <span className="match-card__matchday">
                    {match.matchday != null
                        ? `Matchday ${match.matchday}`
                        : ''}
                </span>

                <Link
                    className="match-card__details"
                    to={matchPath}
                    aria-label={`View details for ${homeName} vs ${awayName}`}
                >
                    View details <span aria-hidden="true">→</span>
                </Link>
            </div>
        </article>
    )
}

export default MatchCard