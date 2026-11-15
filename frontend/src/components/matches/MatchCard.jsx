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

    return (
        <Link
            className="match-card"
            to={`/matches/${match.id}`}
        >
            <div className="match-card__meta">
                <time dateTime={match.kickoffAt}>
                    {formatMatchDateTime(match.kickoffAt)}
                </time>

                <MatchStatus status={match.status} />
            </div>

            <div className="match-card__fixture">
                <MatchTeam team={match.homeTeam} />

                <span className="match-card__result">
                    {result}
                </span>

                <MatchTeam team={match.awayTeam} isAway />
            </div>

            <div className="match-card__footer">
                <span className="match-card__matchday">
                    {match.matchday != null
                        ? `Matchday ${match.matchday}`
                        : ''}
                </span>

                <span className="match-card__details">
                    View details <span aria-hidden="true">→</span>
                </span>
            </div>
        </Link>
    )
}

export default MatchCard